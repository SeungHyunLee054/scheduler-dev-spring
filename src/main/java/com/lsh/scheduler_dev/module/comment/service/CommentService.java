package com.lsh.scheduler_dev.module.comment.service;

import com.lsh.scheduler_dev.common.response.ListResponse;
import com.lsh.scheduler_dev.module.comment.domain.model.Comment;
import com.lsh.scheduler_dev.module.comment.dto.request.CommentCreateDto;
import com.lsh.scheduler_dev.module.comment.dto.request.CommentUpdateDto;
import com.lsh.scheduler_dev.module.comment.dto.response.CommentDto;
import com.lsh.scheduler_dev.module.comment.exception.CommentException;
import com.lsh.scheduler_dev.module.comment.exception.CommentExceptionCode;
import com.lsh.scheduler_dev.module.comment.repository.CommentRepository;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    /**
     * 댓글 저장
     *
     * @param member           댓글을 작성한 유저
     * @param scheduler        댓글이 달린 일정
     * @param commentCreateDto 작성하려는 댓글 내용
     * @return 댓글 정보
     */
    public CommentDto saveComment(Member member, Scheduler scheduler, CommentCreateDto commentCreateDto) {
        Comment savedComment = commentRepository.save(Comment.builder()
                .content(commentCreateDto.getContent())
                .member(member)
                .scheduler(scheduler)
                .build());

        return CommentDto.toDto(savedComment);
    }

    /**
     * 모든 댓글 조회
     *
     * @param schedulerId 일정 id
     * @param pageable    페이지 값
     * @return Page에서 원하는 정보 값만 담은 List를 반환
     */
    public ListResponse<CommentDto> getAllCommentsByScheduler(Long schedulerId, Pageable pageable) {
        return ListResponse
                .toListResponse(commentRepository.findAllBySchedulerIdOrderByModifiedAtDesc(schedulerId, pageable)
                        .map(CommentDto::toDto));
    }

    /**
     * 댓글 수정
     *
     * @param commentId        댓글 id
     * @param memberId         유저 id
     * @param commentUpdateDto 수정할 내용
     * @return 댓글 정보
     */
    @Transactional
    public CommentDto updateComment(Long commentId, Long memberId, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findById(commentId)
                .filter(c -> c.getMember().getId().equals(memberId))
                .orElseThrow(() -> new CommentException(CommentExceptionCode.USER_MISMATCH));

        comment.updateContent(commentUpdateDto.getContent());

        return CommentDto.toDto(comment);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId 댓글 id
     * @param memberId  유저 id
     * @return 삭제된 댓글 정보
     */
    public CommentDto deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .filter(c -> c.getMember().getId().equals(memberId))
                .orElseThrow(() -> new CommentException(CommentExceptionCode.USER_MISMATCH));

        commentRepository.delete(comment);

        return CommentDto.toDto(comment);
    }

}
