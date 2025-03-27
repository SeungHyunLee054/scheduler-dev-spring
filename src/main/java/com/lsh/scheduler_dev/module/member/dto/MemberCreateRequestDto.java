package com.lsh.scheduler_dev.module.member.dto;

import lombok.Getter;

@Getter
public class MemberCreateRequestDto {
    private String name;
    private String email;
    private String password;
}
