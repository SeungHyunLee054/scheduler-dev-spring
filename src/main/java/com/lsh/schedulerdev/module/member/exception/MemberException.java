package com.lsh.schedulerdev.module.member.exception;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.exception.BaseException;

import lombok.Getter;

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
