package com.lsh.scheduler_dev.common.utils.password.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PasswordUtilsExceptionCode {
    ENCRYPT_ERROR("암호화중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR),
    UNEXPECTED_ERROR("예상치 못한 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR)
    ;
    private final String errorMessage;
    private final HttpStatus httpStatus;
}
