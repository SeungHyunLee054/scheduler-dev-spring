package com.lsh.scheduler_dev.common.exception.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;
}
