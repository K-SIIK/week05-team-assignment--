package com.sparta.assignment05.service;

import com.sparta.assignment05.dto.request.LoginRequest;
import com.sparta.assignment05.dto.GlobalResDto;
import com.sparta.assignment05.dto.request.MemberRequest;
import com.sparta.assignment05.dto.response.MemberResponse;
import com.sparta.assignment05.entity.Member;
import com.sparta.assignment05.entity.RefreshToken;
import com.sparta.assignment05.exception.DifferentPasswordsException;
import com.sparta.assignment05.exception.DuplicateEmailException;
import com.sparta.assignment05.exception.NoMemberException;
import com.sparta.assignment05.exception.WrongPasswordsException;
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

    public GlobalResDto<?> signup(MemberRequest memberRequest) throws DuplicateEmailException, DifferentPasswordsException {
        // email 같은 회원 있는지 검사
        isDuplicatedEmail(memberRequest);
        // 비밀번호 2개 일치 하는지 검사
        isSamePasswords(memberRequest);

        Member member = Member.builder()
                .email(memberRequest.getEmail())
                .nickName(memberRequest.getNickName())
                // 인코딩된 비밀번호 문자열을 멤버객체에 저장.
                .password(passwordEncoder.encode(memberRequest.getPassword()))
                .build();

        memberRepository.save(member);

        return GlobalResDto.success(new MemberResponse(member));
    }

    public GlobalResDto<?> login(LoginRequest loginRequest, HttpServletResponse response) throws WrongPasswordsException {
        // 이메일 있는지 확인
        Member member = memberRepository.findByEmail(loginRequest.getEmail()).orElseThrow(NoMemberException::new);
        // 해당 이메일의 비밀번호가 맞는지 확인
        member.validatePassword(passwordEncoder, loginRequest.getPassword());
        // 로그인 성공하면 토큰 발급
        TokenDto tokenDto = jwtUtil.createAllToken(member);
        // repository 에서 토큰 확인 후 이상 있으면 재발급, 없으면 새로 발급
        checkToken(loginRequest, tokenDto);
        // 헤더에 토큰 첨부
        attachTokenToHeader(tokenDto, response);
        return GlobalResDto.success(new MemberResponse(member));
    }

    public GlobalResDto<?> logout(Member member) {
        memberRepository.delete(member);
        jwtUtil.deleteToken(member);
        return GlobalResDto.success("Log out");
    }

    // 회원가입 시 중복된 계정 있는지 확인
    private void isDuplicatedEmail(MemberRequest memberRequest) throws DuplicateEmailException {
        if (memberRepository.findByEmail(memberRequest.getEmail()).isPresent())
            throw new DuplicateEmailException(memberRequest.getEmail());
    }

    // 회원가입시 입력받은 비밀번호 2개 같은지 확인
    private void isSamePasswords(MemberRequest memberRequest) throws DifferentPasswordsException {
        if (!memberRequest.getPassword().equals(memberRequest.getPasswordCheck())) {
            throw new DifferentPasswordsException();
        }
    }


    // 토큰 유무 확인하고 있으면 갱신해주고, 없으면 생성해주기
    private void checkToken(LoginRequest loginRequest, TokenDto tokenDto) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(loginRequest.getEmail());

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newRefreshToken = new RefreshToken(tokenDto.getRefreshToken(), loginRequest.getEmail());
            refreshTokenRepository.save(newRefreshToken);
        }
    }

    // 토큰 헤더에 첨부
    private void attachTokenToHeader(TokenDto tokenDto, HttpServletResponse response) {
        response.setHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.setHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

}
