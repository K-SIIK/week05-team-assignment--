package com.sparta.assignment05.service;

import com.sparta.assignment05.dto.request.LoginRequest;
import com.sparta.assignment05.dto.GlobalResDto;
import com.sparta.assignment05.dto.request.MemberRequest;
import com.sparta.assignment05.dto.response.MemberResponse;
import com.sparta.assignment05.entity.Member;
import com.sparta.assignment05.entity.RefreshToken;
import com.sparta.assignment05.jwt.JwtUtil;
import com.sparta.assignment05.jwt.TokenDto;
import com.sparta.assignment05.repository.MemberRepository;
import com.sparta.assignment05.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public GlobalResDto<?> signup(MemberRequest memberRequest) {
        // email 같은 회원 있는지 검사
        if (memberRepository.findByEmail(memberRequest.getEmail()).isPresent()) {
            return GlobalResDto.fail("DUPLICATED_EMAIL", "중복된 email 입니다.");
        }

        // 비밀번호 2개 일치 하는지 검사
        if (!memberRequest.getPassword().equals(memberRequest.getPasswordCheck())) {
            return GlobalResDto.fail("PASSWORD_NOT_MATCH", "비밀번호가 일치하지 않습니다.");
        }

        Member member = Member.builder()
                .email(memberRequest.getEmail())
                .nickName(memberRequest.getNickName())
                // 인코딩된 비밀번호 문자열을 멤버객체에 저장.
                .password(passwordEncoder.encode(memberRequest.getPassword()))
                .build();

        memberRepository.save(member);
        MemberResponse memberResponse = MemberResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();

        return GlobalResDto.success(member.getResponceInstance());
    }

    public GlobalResDto<?> login(LoginRequest loginRequest, HttpServletResponse response) {
        Optional<Member> member = memberRepository.findByEmail(loginRequest.getEmail());

        if (member.isEmpty()) {
            return GlobalResDto.fail("NOT_EXIST_MEMBER", "회원이 아닙니다.");
        }

        if (!member.get().validatePassword(passwordEncoder, loginRequest.getPassword())) {
            return GlobalResDto.fail("WRONG_PASSWORD", "비밀번호가 일치하지 않습니다.");
        }

        // 로그인 성공하면 토큰 발급
        TokenDto tokenDto = jwtUtil.createAllToken(member.get());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(loginRequest.getEmail());

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newRefreshToken = new RefreshToken(tokenDto.getRefreshToken(),loginRequest.getEmail());
            refreshTokenRepository.save(newRefreshToken);
        }

        // 헤더에 토큰 첨부
        attachTokenToHeader(tokenDto, response);

        MemberResponse memberResponse = MemberResponse.builder()
                .memberId(member.get().getId())
                .email(member.get().getEmail())
                .nickName(member.get().getNickName())
                .createdAt(member.get().getCreatedAt())
                .modifiedAt(member.get().getModifiedAt())
                .build();

        return GlobalResDto.success(memberResponse);
    }

    private void attachTokenToHeader(TokenDto tokenDto, HttpServletResponse response) {
        response.setHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.setHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

    public GlobalResDto<?> logout(UserDetailsImpl userDetails) {

        if (null == userDetails.getMember()) {
            return GlobalResDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        jwtUtil.deleteToken(userDetails.getMember());

        return GlobalResDto.success("SUCCESS");
    }


}
