package com.lsh.scheduler_dev.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ListResponse<T> {
    private long totalElements;
    private int totalPages;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    @Builder.Default
    private List<T> content = new ArrayList<>();

    public static <T> ListResponse<T> from(Page<T> page) {
        return ListResponse.<T>builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNextPage(page.hasNext())
                .hasPreviousPage(page.hasPrevious())
                .content(page.getContent())
                .build();
    }
}
