package com.lsh.schedulerdev.domain.scheduler.code;

import org.springframework.http.HttpStatus;

import com.lsh.schedulerdev.common.response.ResponseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SchedulerSuccessCode implements ResponseCode {
	SCHEDULER_CREATE_SUCCESS(true, "일정 생성 성공", HttpStatus.CREATED),
	SCHEDULER_READ_ALL_SUCCESS(true, "모든 일정 조회 성공", HttpStatus.OK),
	SCHEDULER_UPDATE_SUCCESS(true, "일정 수정 성공", HttpStatus.OK),
	SCHEDULER_DELETE_SUCCESS(true, "일정 삭제 성공", HttpStatus.OK);

	private final boolean success;
	private final String message;
	private final HttpStatus httpStatus;
}
