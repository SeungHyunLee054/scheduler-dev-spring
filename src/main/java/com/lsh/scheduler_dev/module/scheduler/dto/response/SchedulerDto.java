package com.lsh.scheduler_dev.module.scheduler.dto.response;

import java.time.LocalDateTime;

import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;

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
			.memberId(scheduler.getMember().getId())
			.name(scheduler.getMember().getName())
			.title(scheduler.getTitle())
			.content(scheduler.getContent())
			.commentCount(scheduler.getCommentCount())
			.createdAt(scheduler.getCreatedAt())
			.modifiedAt(scheduler.getModifiedAt())
			.build();
	}
}
