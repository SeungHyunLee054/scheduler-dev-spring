package com.lsh.schedulerdev.domain.member.exception;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.exception.BaseException;
import com.lsh.schedulerdev.domain.member.code.MemberExceptionCode;

import lombok.Getter;

@Getter
public class MemberException extends BaseException {
	private final MemberExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String errorMessage;

	public MemberException(MemberExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getHttpStatus();
		this.errorMessage = errorCode.getMessage();
	}
}
