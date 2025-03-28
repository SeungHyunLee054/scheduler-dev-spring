package com.lsh.scheduler_dev.module.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentExceptionCode {
    USER_MISMATCH("로그인한 유저와 작성자가 다릅니다.", HttpStatus.FORBIDDEN),
    ;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
