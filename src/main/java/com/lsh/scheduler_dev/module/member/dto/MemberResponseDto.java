package com.lsh.scheduler_dev.module.member.dto;

import com.lsh.scheduler_dev.module.member.domain.model.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberResponseDto {
    private Long memberId;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static MemberResponseDto toDto(Member member) {
        return MemberResponseDto.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
    }
}
