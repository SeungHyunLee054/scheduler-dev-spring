package com.lsh.schedulerdev.domain.member.dto;

import com.lsh.schedulerdev.domain.member.entity.Member;

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
