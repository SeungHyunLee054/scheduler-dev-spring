package com.lsh.schedulerdev.domain.scheduler.exception;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.exception.BaseException;
import com.lsh.schedulerdev.domain.scheduler.code.SchedulerExceptionCode;

import lombok.Getter;

@Getter
public class SchedulerException extends BaseException {
	private final SchedulerExceptionCode errorCode;
	private final HttpStatus httpStatus;
	private final String errorMessage;

	public SchedulerException(SchedulerExceptionCode errorCode) {
		this.errorCode = errorCode;
		this.httpStatus = errorCode.getHttpStatus();
		this.errorMessage = errorCode.getMessage();
	}
}
