package com.lsh.schedulerdev.domain.comment.dto.response;

import java.time.LocalDateTime;

import com.lsh.schedulerdev.domain.comment.entity.Comment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentDto {
	private Long commentId;
	private Long memberId;
	private Long schedulerId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public static CommentDto from(Comment comment) {
		return CommentDto.builder()
			.commentId(comment.getId())
			.memberId(comment.getMemberId())
			.schedulerId(comment.getSchedulerId())
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.modifiedAt(comment.getModifiedAt())
			.build();
	}
}
