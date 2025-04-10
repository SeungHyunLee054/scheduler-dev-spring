package com.lsh.schedulerdev.domain.comment.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreateDto {
	@Size(max = 50, message = "내용은 50글자 내여야 합니다.")
	private String content;
}
