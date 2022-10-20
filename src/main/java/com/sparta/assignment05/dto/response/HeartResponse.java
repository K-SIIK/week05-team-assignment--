package com.sparta.assignment05.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.assignment05.entity.Heart;
import lombok.Getter;

@Getter
public class HeartResponse {

    private final Long heartId;
    private final Long boardId;
    private final String email;

    public HeartResponse(Heart heart) {
        this.heartId = heart.getId();
        this.boardId = heart.getBoard().getId();
        this.email = heart.getMember().getEmail();
    }
}
