package com.sparta.assignment05.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.assignment05.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardResponse {

    private Long boardId;
    private String title;
    private String content;
    private String author;
    private Long heartCnt;
    private Long commentCnt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponse> commentList;

    public static List<CommentResponse> commentToResponse(List<Comment> commentList) {
        List<CommentResponse> responseList = new ArrayList<>();

        for (Comment comment : commentList) {
            responseList.add(new CommentResponse(comment));
        }

        return responseList;
    }
}
