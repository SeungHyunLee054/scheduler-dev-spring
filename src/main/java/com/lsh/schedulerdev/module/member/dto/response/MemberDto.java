package com.lsh.schedulerdev.module.member.dto.response;

import java.time.LocalDateTime;

import com.lsh.schedulerdev.module.member.domain.model.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {
	private Long memberId;
	private String name;
	private String email;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public static MemberDto from(Member member) {
		return MemberDto.builder()
			.memberId(member.getId())
			.name(member.getName())
			.email(member.getEmail())
			.createdAt(member.getCreatedAt())
			.modifiedAt(member.getModifiedAt())
			.build();
	}
}
