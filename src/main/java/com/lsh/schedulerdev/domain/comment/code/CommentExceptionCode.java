package com.lsh.schedulerdev.domain.comment.code;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.response.ResponseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentExceptionCode implements ResponseCode {
	USER_MISMATCH(false, "로그인한 유저와 작성자가 다릅니다.", HttpStatus.UNAUTHORIZED),
	COMMENT_NOT_FOUND(false, "댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

	private final boolean success;
	private final String message;
	private final HttpStatus httpStatus;
}
