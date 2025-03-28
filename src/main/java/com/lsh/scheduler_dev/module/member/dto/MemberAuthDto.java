package com.lsh.scheduler_dev.module.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberAuthDto {
    private Long memberId;
    private String email;
}
