package com.sparta.assignment05.controller;

import com.sparta.assignment05.dto.GlobalResDto;
import com.sparta.assignment05.service.CommentService;
import com.sparta.assignment05.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/auth/boards/{boardId}/comments")
    public GlobalResDto<?> createComment(@PathVariable Long boardId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @RequestBody String text) {

        return commentService.createComment(boardId, userDetails.getMember(), text);
    }

    @GetMapping("/boards/{boardId}/comments")
    public GlobalResDto<?> getCommentList(@PathVariable Long boardId) {
        return commentService.getCommentList(boardId);
    }

    @PutMapping("/auth/boards/{boardId}/comments/{commentId}")
    public GlobalResDto<?> updateComment(@PathVariable Long boardId,
                                         @PathVariable Long commentId,
                                         @RequestBody String text,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return commentService.updateComment(boardId, commentId, text, userDetails.getMember());
    }

    @DeleteMapping("/auth/boards/{boardId}/comments/{commentId}")
    public GlobalResDto<?> deleteComment(@PathVariable Long boardId,
                                         @PathVariable Long commentId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return commentService.deleteComment(boardId, commentId, userDetails.getMember());
    }


}
