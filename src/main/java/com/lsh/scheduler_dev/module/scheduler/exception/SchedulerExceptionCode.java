package com.lsh.scheduler_dev.module.scheduler.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SchedulerExceptionCode {
    USER_MISMATCH("로그인한 유저와 작성자가 다릅니다.", HttpStatus.FORBIDDEN),
    SCHEDULER_NOT_FOUND("일정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ;

    private final String errorMessage;
    private final HttpStatus httpStatus;
}
