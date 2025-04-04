package com.lsh.schedulerdev.domain.member.code;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.response.ResponseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberExceptionCode implements ResponseCode {
	USER_MISMATCH(false, "로그인한 유저와 다릅니다.", HttpStatus.FORBIDDEN),
	ALREADY_EXIST_MEMBER(false, "이미 존재하는 유저가 있습니다", HttpStatus.CONFLICT),
	MEMBER_NOT_FOUND(false, "유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	FAIL_SIGN_IN(false, "로그인에 실패하였습니다.", HttpStatus.UNAUTHORIZED);

	private final boolean success;
	private final String message;
	private final HttpStatus httpStatus;
}
