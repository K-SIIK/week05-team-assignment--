package com.sparta.assignment05.controller;

import com.sparta.assignment05.dto.request.LoginRequest;
import com.sparta.assignment05.dto.GlobalResDto;
import com.sparta.assignment05.dto.request.MemberRequest;
import com.sparta.assignment05.jwt.JwtUtil;
import com.sparta.assignment05.service.MemberService;
import com.sparta.assignment05.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping("/members/signup") // 해당 객체가 유효한지 검증해주는 어노테이션 @NotBlank 같은거 검증
    public GlobalResDto<?> signup(@RequestBody @Valid MemberRequest memberRequest) {
        return memberService.signup(memberRequest);
    }

    @PostMapping("/members/login")
    public GlobalResDto<?> login(@RequestBody @Valid LoginRequest loginRequest,
                                 HttpServletResponse response) {
        return memberService.login(loginRequest, response);
    }

    @PostMapping("/members/logout")
    public GlobalResDto<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memberService.logout(userDetails);
    }

    // 엑세스토큰 재발급 메서드
    // @AuthenticationPrincipal
    @GetMapping("/issue/token")
    public GlobalResDto<?> issuedToken(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                       HttpServletResponse response) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, jwtUtil.createToken(userDetails.getMember(), "Access"));
        return GlobalResDto.success("REISSUE_ACCESS_TOKEN");
    }
}
