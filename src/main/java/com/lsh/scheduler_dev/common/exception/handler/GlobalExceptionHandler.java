package com.lsh.scheduler_dev.common.exception.handler;

import com.lsh.scheduler_dev.common.exception.BaseException;
import com.lsh.scheduler_dev.common.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> CustomExceptionHandler(BaseException e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));

        return ResponseEntity.status(e.getHttpStatus())
                .body(ErrorResponse.builder()
                        .errorCode(e.getErrorCode().name())
                        .errorMessage(e.getErrorMessage())
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
