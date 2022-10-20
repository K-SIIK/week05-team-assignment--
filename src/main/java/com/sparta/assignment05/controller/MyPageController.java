package com.sparta.assignment05.controller;

import com.sparta.assignment05.dto.GlobalResDto;
import com.sparta.assignment05.service.MyPageService;
import com.sparta.assignment05.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class MyPageController {

    private final MyPageService myPageService;

    // url 에 myPage 넣어주는게 더 restful 할 것 같아서 명세대로 안함
    @GetMapping("/mypages/boards")
    public GlobalResDto<?> getMyBoards(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMyBoards(userDetails.getMember());
    }

    @GetMapping("/mypages/comments")
    public GlobalResDto<?> getMyComments(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMyComments(userDetails.getMember());
    }


    @GetMapping("/mypages/hearts")
    public GlobalResDto<?> getMyHearts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMyHearts(userDetails.getMember());
    }
}
