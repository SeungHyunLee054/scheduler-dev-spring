package com.lsh.scheduler_dev.module.comment.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentExceptionCode {
	USER_MISMATCH("로그인한 유저와 작성자가 다릅니다.", HttpStatus.UNAUTHORIZED),
	COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
	private final String errorMessage;
	private final HttpStatus httpStatus;
}
