package com.lsh.schedulerdev.common.utils.password.exception;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.exception.BaseException;

import lombok.Getter;

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
