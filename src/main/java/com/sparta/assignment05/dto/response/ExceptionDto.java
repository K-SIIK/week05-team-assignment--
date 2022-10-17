package com.sparta.assignment05.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExceptionDto {

    private String msg;
    private int statusCode;

    public ExceptionDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }

}
