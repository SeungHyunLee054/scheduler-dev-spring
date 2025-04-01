package com.lsh.scheduler_dev.common.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponses<T> {
	private long totalElements;
	private int totalPages;
	private boolean hasNextPage;
	private boolean hasPreviousPage;
	private String message;
	@Builder.Default
	private List<T> result = new ArrayList<>();

	public static <T> CommonResponses<T> from(String message, Page<T> page) {
		return CommonResponses.<T>builder()
			.totalElements(page.getTotalElements())
			.totalPages(page.getTotalPages())
			.hasNextPage(page.hasNext())
			.hasPreviousPage(page.hasPrevious())
			.message(message)
			.result(page.getContent())
			.build();
	}
}
