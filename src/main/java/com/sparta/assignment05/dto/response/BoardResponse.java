package com.sparta.assignment05.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {

    private Long boardId;
    private String title;
    private String content;
    private String author;
    private Long heartCnt;
    private Long commentCnt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<String> commentList;


}
