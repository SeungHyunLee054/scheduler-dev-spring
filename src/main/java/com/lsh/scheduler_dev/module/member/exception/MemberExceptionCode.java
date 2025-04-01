package com.lsh.scheduler_dev.module.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberExceptionCode {
    USER_MISMATCH("로그인한 유저와 다릅니다.", HttpStatus.FORBIDDEN),
    ALREADY_EXIST_MEMBER("이미 존재하는 유저가 있습니다", HttpStatus.CONFLICT),
    MEMBER_NOT_FOUND("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FAIL_SIGN_IN("이메일 또는 비밀번호가 틀립니다.", HttpStatus.UNAUTHORIZED);

    private final String errorMessage;
    private final HttpStatus httpStatus;
}
