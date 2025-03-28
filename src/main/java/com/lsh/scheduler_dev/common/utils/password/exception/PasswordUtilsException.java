package com.lsh.scheduler_dev.common.utils.password.exception;

import com.lsh.scheduler_dev.common.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PasswordUtilsException extends BaseException {
    private final PasswordUtilsExceptionCode errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    public PasswordUtilsException(PasswordUtilsExceptionCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getErrorMessage();
        this.httpStatus = errorCode.getHttpStatus();
    }
}
