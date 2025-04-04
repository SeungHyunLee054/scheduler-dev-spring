package com.lsh.schedulerdev.common.exception;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.response.ResponseCode;

public abstract class BaseException extends RuntimeException {
	public abstract ResponseCode getErrorCode();

	public abstract HttpStatus getHttpStatus();

}
