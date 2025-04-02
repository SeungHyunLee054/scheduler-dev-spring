package com.lsh.schedulerdev.common.utils;

import com.lsh.schedulerdev.common.exception.BaseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtils {
	public static void logError(Throwable throwable) {
		if (throwable instanceof BaseException baseException) {
			log.error("예외 발생: {} (ErrorCode: {})", baseException.getMessage(), baseException.getErrorCode());
		}

		StackTraceElement firstStackTrace = throwable.getStackTrace()[0];
		log.error("발생 위치: {}:{} - Thread: {}, Method: {}",
			firstStackTrace.getClassName(), firstStackTrace.getLineNumber(),
			Thread.currentThread().getName(), firstStackTrace.getMethodName());
	}
}
