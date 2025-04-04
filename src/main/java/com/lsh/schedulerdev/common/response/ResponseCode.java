package com.lsh.schedulerdev.common.response;

import org.springframework.http.HttpStatus;

public interface ResponseCode {
	boolean isSuccess();

	HttpStatus getHttpStatus();

	String getMessage();
}
