package com.lsh.scheduler_dev.module.member.exception;

import com.lsh.scheduler_dev.common.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberException extends BaseException {
    private final MemberExceptionCode errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    public MemberException(MemberExceptionCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getHttpStatus();
        this.errorMessage = errorCode.getErrorMessage();
    }
}
