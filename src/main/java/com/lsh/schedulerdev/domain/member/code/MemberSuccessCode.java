package com.lsh.schedulerdev.domain.member.code;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.response.ResponseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberSuccessCode implements ResponseCode {
	MEMBER_SIGN_UP_SUCCESS(true, "회원 가입 성공", HttpStatus.CREATED),
	MEMBER_SIGN_IN_SUCCESS(true, "로그인 성공", HttpStatus.OK),
	MEMBER_READ_ALL_SUCCESS(true, "모든 유저 조회 성공", HttpStatus.OK),
	MEMBER_UPDATE_SUCCESS(true, "유저 수정 성공", HttpStatus.OK),
	MEMBER_DELETE_SUCCESS(true, "유저 삭제 성공", HttpStatus.OK);

	private final boolean success;
	private final String message;
	private final HttpStatus httpStatus;
}
