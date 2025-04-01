package com.lsh.schedulerdev.common.exception.handler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lsh.schedulerdev.common.exception.BaseException;
import com.lsh.schedulerdev.common.exception.dto.ErrorResponse;
import com.lsh.schedulerdev.common.utils.log.LogErrorUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> customExceptionHandler(BaseException baseException) {
		log.error("예외 발생: {} (ErrorCode: {})", baseException.getMessage(), baseException.getErrorCode());
		LogErrorUtils.logError(baseException);

		return ResponseEntity.status(baseException.getHttpStatus())
			.body(ErrorResponse.builder()
				.errorCode(baseException.getErrorCode().name())
				.errorMessage(baseException.getErrorMessage())
				.build());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ErrorResponse>> inputValidationExceptionHandler(BindingResult result) {
		log.error(result.getFieldErrors().toString());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(result.getFieldErrors().stream()
				.map(e -> ErrorResponse.builder()
					.errorCode(e.getCode())
					.errorMessage(e.getDefaultMessage())
					.build())
				.toList());
	}

}
