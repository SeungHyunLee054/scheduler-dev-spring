package com.lsh.scheduler_dev.module.scheduler.dto;

import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SchedulerResponseDto {
    private Long schedulerId;
    private Long memberId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static SchedulerResponseDto toDto(Scheduler scheduler) {
        return SchedulerResponseDto.builder()
                .schedulerId(scheduler.getId())
                .memberId(scheduler.getMember().getId())
                .title(scheduler.getTitle())
                .content(scheduler.getContent())
                .createdAt(scheduler.getCreatedAt())
                .modifiedAt(scheduler.getModifiedAt())
                .build();
    }
}
