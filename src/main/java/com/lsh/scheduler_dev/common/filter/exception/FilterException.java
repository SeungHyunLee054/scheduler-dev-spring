package com.lsh.scheduler_dev.common.filter.exception;

import com.lsh.scheduler_dev.common.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FilterException extends BaseException {
    private final FilterExceptionCode errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    public FilterException(FilterExceptionCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getErrorMessage();
        this.httpStatus = errorCode.getHttpStatus();
    }
}
