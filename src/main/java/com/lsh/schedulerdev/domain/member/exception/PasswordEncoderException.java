package com.lsh.schedulerdev.domain.member.exception;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.exception.BaseException;
import com.lsh.schedulerdev.domain.member.code.PasswordEncoderExceptionCode;

import lombok.Getter;

@Getter
public class PasswordEncoderException extends BaseException {
	private final PasswordEncoderExceptionCode errorCode;
	private final String errorMessage;
	private final HttpStatus httpStatus;

	public PasswordEncoderException(PasswordEncoderExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorCode.getMessage();
		this.httpStatus = errorCode.getHttpStatus();
	}
}
