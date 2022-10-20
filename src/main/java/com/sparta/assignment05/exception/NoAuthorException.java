package com.sparta.assignment05.exception;

public class NoAuthorException extends Throwable {
    public NoAuthorException(String email) {
        super(email);
    }
}
