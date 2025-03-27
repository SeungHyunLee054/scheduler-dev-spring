package com.lsh.scheduler_dev.common.security.jwt.provieder.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberAuthDto {
    private Long memberId;
    private String email;
}
