package com.lsh.schedulerdev.domain.comment.exception;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.exception.BaseException;

import lombok.Getter;

@Getter
public class CommentException extends BaseException {
	private final CommentExceptionCode errorCode;
	private final String errorMessage;
	private final HttpStatus httpStatus;

	public CommentException(CommentExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorCode.getErrorMessage();
		this.httpStatus = errorCode.getHttpStatus();
	}
}
