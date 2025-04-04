package com.lsh.schedulerdev.domain.scheduler.dto.response;

import java.time.LocalDateTime;

import com.lsh.schedulerdev.domain.scheduler.entity.Scheduler;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SchedulerDto {
	private Long schedulerId;
	private Long memberId;
	private String name;
	private String title;
	private String content;
	private Integer commentCount;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public static SchedulerDto from(Scheduler scheduler) {
		return SchedulerDto.builder()
			.schedulerId(scheduler.getId())
			.memberId(scheduler.getMemberId())
			.name(scheduler.getMemberName())
			.title(scheduler.getTitle())
			.content(scheduler.getContent())
			.commentCount(scheduler.getCommentCount())
			.createdAt(scheduler.getCreatedAt())
			.modifiedAt(scheduler.getModifiedAt())
			.build();
	}
}
