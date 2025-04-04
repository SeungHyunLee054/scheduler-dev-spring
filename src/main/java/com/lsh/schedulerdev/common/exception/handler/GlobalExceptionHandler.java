package com.lsh.schedulerdev.common.exception.handler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lsh.schedulerdev.common.exception.BaseException;
import com.lsh.schedulerdev.common.exception.dto.ValidationError;
import com.lsh.schedulerdev.common.response.CommonResponse;
import com.lsh.schedulerdev.common.response.CommonResponses;
import com.lsh.schedulerdev.common.response.ResponseCode;
import com.lsh.schedulerdev.common.utils.LogUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BaseException.class)
	public ResponseEntity<CommonResponse<ResponseCode>> customExceptionHandler(BaseException baseException) {
		LogUtils.logError(baseException);

		return ResponseEntity.status(baseException.getHttpStatus())
			.body(CommonResponse.from(baseException.getErrorCode()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CommonResponses<ValidationError>> inputValidationExceptionHandler(BindingResult result) {
		log.error(result.getFieldErrors().toString());

		List<ValidationError> validationErrors = result.getFieldErrors().stream()
			.map(fieldError -> ValidationError.builder()
				.field(fieldError.getField())
				.message(fieldError.getDefaultMessage())
				.code(fieldError.getCode())
				.build())
			.toList();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(CommonResponses.of(false, HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다.",
				validationErrors));
	}

}
