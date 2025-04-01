package com.lsh.schedulerdev.common.utils.password.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PasswordUtilsExceptionCode {
	ENCRYPT_ERROR("암호화중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR),
	UNEXPECTED_ERROR("예상치 못한 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
	private final String errorMessage;
	private final HttpStatus httpStatus;
}
