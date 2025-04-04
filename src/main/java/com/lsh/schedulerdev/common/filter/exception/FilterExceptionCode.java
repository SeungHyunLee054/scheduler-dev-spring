package com.lsh.schedulerdev.common.filter.exception;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.response.ResponseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilterExceptionCode implements ResponseCode {
	INVALID_SESSION(false, "유효하지 않은 Session 입니다.", HttpStatus.UNAUTHORIZED);

	private final boolean success;
	private final String message;
	private final HttpStatus httpStatus;
}
