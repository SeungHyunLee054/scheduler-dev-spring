package com.lsh.schedulerdev.module.member.exception;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.exception.BaseException;

import lombok.Getter;

@Getter
public class PasswordEncoderException extends BaseException {
	private final PasswordEncoderExceptionCode errorCode;
	private final String errorMessage;
	private final HttpStatus httpStatus;

	public PasswordEncoderException(PasswordEncoderExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorCode.getErrorMessage();
		this.httpStatus = errorCode.getHttpStatus();
	}
}
