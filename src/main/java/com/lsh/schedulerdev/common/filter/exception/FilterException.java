package com.lsh.schedulerdev.common.filter.exception;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.exception.BaseException;

import lombok.Getter;

@Getter
public class FilterException extends BaseException {
	private final FilterExceptionCode errorCode;
	private final String errorMessage;
	private final HttpStatus httpStatus;

	public FilterException(FilterExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorCode.getMessage();
		this.httpStatus = errorCode.getHttpStatus();
	}
}
