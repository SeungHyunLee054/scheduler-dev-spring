package com.lsh.schedulerdev.domain.member.code;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.response.ResponseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PasswordEncoderExceptionCode implements ResponseCode {
	ENCRYPT_ERROR(false, "암호화중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR),
	UNEXPECTED_ERROR(false, "예상치 못한 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);

	private final boolean success;
	private final String message;
	private final HttpStatus httpStatus;
}
