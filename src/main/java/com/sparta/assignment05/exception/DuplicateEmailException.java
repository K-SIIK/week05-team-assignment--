package com.sparta.assignment05.exception;

public class DuplicateEmailException extends Throwable {

    public DuplicateEmailException(String email) {
        super(email);
    }
}
