package com.lsh.scheduler_dev.module.comment.dto.response;

import com.lsh.scheduler_dev.module.comment.domain.model.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
                .memberId(comment.getMember().getId())
                .schedulerId(comment.getScheduler().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }
}
