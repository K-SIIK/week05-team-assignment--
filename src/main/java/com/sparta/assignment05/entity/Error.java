package com.sparta.assignment05.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Error {

    private String field;
    private String message;
}
