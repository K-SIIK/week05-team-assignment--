package com.sparta.assignment05.dto.response;

import com.sparta.assignment05.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long commentId;
    private String comment;
    private String author;
    private Long boardId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.comment = comment.getComment();
        this.author = comment.getMember().getEmail();
        this.boardId = comment.getBoard().getId();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
