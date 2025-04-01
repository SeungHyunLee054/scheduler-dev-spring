package com.lsh.scheduler_dev.common.log.error;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LogErrorUtils {
	public static void logError(Throwable throwable) {
		StackTraceElement firstStackTrace = throwable.getStackTrace()[0];
		log.error("발생 위치: {}:{} - Thread: {}, Method: {}",
			firstStackTrace.getClassName(), firstStackTrace.getLineNumber(),
			Thread.currentThread().getName(), firstStackTrace.getMethodName());
	}
}
