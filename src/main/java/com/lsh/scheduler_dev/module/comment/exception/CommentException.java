package com.lsh.scheduler_dev.module.comment.exception;

import com.lsh.scheduler_dev.common.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommentException extends BaseException {
    private final CommentExceptionCode errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    public CommentException(CommentExceptionCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getErrorMessage();
        this.httpStatus = errorCode.getHttpStatus();
    }
}
