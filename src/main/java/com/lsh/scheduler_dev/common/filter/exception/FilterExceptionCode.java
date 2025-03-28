package com.lsh.scheduler_dev.common.filter.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FilterExceptionCode {
    INVALID_SESSION("유효하지 않은 Session 입니다.",HttpStatus.UNAUTHORIZED)
    ;

    private final String errorMessage;
    private final HttpStatus httpStatus;
}
