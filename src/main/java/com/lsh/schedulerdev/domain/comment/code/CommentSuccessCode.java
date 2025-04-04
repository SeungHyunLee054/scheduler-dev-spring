package com.lsh.schedulerdev.domain.comment.code;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.response.ResponseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentSuccessCode implements ResponseCode {
	COMMENT_CREATE_SUCCESS(true, "해당 일정에 댓글 생성 성공", HttpStatus.CREATED),
	COMMENT_READ_ALL_SUCCESS(true, "해당 일정의 모든 댓글 조회 성공", HttpStatus.OK),
	COMMENT_UPDATE_SUCCESS(true, "해당 일정의 해당 댓글 수정 성공", HttpStatus.OK),
	COMMENT_DELETE_SUCCESS(true, "해당 일정의 해당 댓글 삭제 성공", HttpStatus.OK);

	private final boolean success;
	private final String message;
	private final HttpStatus httpStatus;
}
