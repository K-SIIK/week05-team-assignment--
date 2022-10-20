package com.sparta.assignment05.exception;

public class NotFoundAccountException extends Throwable {
    public NotFoundAccountException(String email) {
        super(email);
    }
}
