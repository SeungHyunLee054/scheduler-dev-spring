package com.lsh.scheduler_dev.module.member.dto;

import com.lsh.scheduler_dev.module.member.domain.model.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberAuthDto {
	private Long memberId;
	private String email;

	public static MemberAuthDto from(Member member) {
		return MemberAuthDto.builder()
			.memberId(member.getId())
			.email(member.getEmail())
			.build();
	}
}
