package com.lsh.scheduler_dev.module.scheduler.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SchedulerCreateDto {
    @NotNull(message = "제목은 필수 입력 값입니다.")
    @NotBlank(message = "제목은 공백이 아니어야 합니다.")
    @Size(max = 10, message = "제목은 10글자 내여야 합니다.")
    private String title;

    @NotNull(message = "내용은 필수 입력 값입니다.")
    @Size(max = 200, message = "내용은 200글자 내여야 합니다.")
    private String content;
}
