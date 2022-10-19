package com.sparta.assignment05.dto;

import com.sparta.assignment05.entity.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GlobalResDto<T> {

    private boolean success;
    private T data;
    private Error error;

    // success 메서드
    public static <T> GlobalResDto<T> success(T data) {
        return new GlobalResDto<>(true, data, null);
    }


    public static <T> GlobalResDto<T> fail(String code, String message) {
        return new GlobalResDto<>(false, null, new Error(code, message));
    }
}