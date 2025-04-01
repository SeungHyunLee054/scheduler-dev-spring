package com.lsh.scheduler_dev.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse<T> {
	private String message;
	private T result;

	public static <T> CommonResponse<T> of(String message, T result) {
		return CommonResponse.<T>builder()
			.message(message)
			.result(result)
			.build();
	}
}
