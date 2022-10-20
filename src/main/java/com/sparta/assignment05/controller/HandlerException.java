package com.sparta.assignment05.controller;

import com.sparta.assignment05.dto.GlobalResDto;
import com.sparta.assignment05.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class HandlerException {

    @ExceptionHandler(NotFoundAccountException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GlobalResDto<?> notFoundAccountException(NotFoundAccountException e) {
        return GlobalResDto.fail("NOT_FOUND_ACCOUNT", e.getMessage() + "계정을 찾을 수 없습니다.");
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public GlobalResDto<?> duplicateEmailException(DuplicateEmailException e) {
        return GlobalResDto.fail("DUPLICATED_EMAIL", e.getMessage() + " 은 중복된 email 입니다.");
    }

    @ExceptionHandler(DifferentPasswordsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public GlobalResDto<?> differentPasswordsException() {
        return GlobalResDto.fail("PASSWORD_NOT_MATCH", "비밀번호가 일치하지 않습니다.");
    }

    @ExceptionHandler(WrongPasswordsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public GlobalResDto<?> notMatchPasswordsException() {
        return GlobalResDto.fail("WRONG_PASSWORD", "비밀번호가 일치하지 않습니다.");
    }

    @ExceptionHandler(NotExistBoardException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GlobalResDto<?> notExistBoardException() {
        return GlobalResDto.fail("NOT_EXIST_BOARD", "존재하지 않는 게시물입니다.");
    }

    @ExceptionHandler(NoAuthorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GlobalResDto<?> noAuthorException(NoAuthorException e) {
        return GlobalResDto.fail("NO_AUTHOR", e.getMessage() + " 님은 작성자가 아닙니다.");
    }

    @ExceptionHandler(NotExistCommentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GlobalResDto<?> notExistCommentException() {
        return GlobalResDto.fail("NOT_EXIST_COMMENT", "존재하지 않는 댓글입니다.");
    }
}
