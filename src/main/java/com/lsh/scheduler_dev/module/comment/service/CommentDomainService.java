package com.lsh.scheduler_dev.module.comment.service;

import com.lsh.scheduler_dev.module.comment.domain.model.Comment;
import com.lsh.scheduler_dev.module.comment.dto.request.CommentCreateDto;
import com.lsh.scheduler_dev.module.comment.dto.request.CommentUpdateDto;
import com.lsh.scheduler_dev.module.comment.exception.CommentException;
import com.lsh.scheduler_dev.module.comment.exception.CommentExceptionCode;
import com.lsh.scheduler_dev.module.comment.repository.CommentRepository;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentDomainService {
    private final CommentRepository commentRepository;

    /**
     * 댓글 저장
     *
     * @param member           댓글을 작성한 유저
     * @param scheduler        댓글이 달린 일정
     * @param commentCreateDto 작성하려는 댓글 내용
     * @return 댓글 정보
     */
    public Comment saveComment(Member member, Scheduler scheduler, CommentCreateDto commentCreateDto) {
        return commentRepository.save(Comment.builder()
                .content(commentCreateDto.getContent())
                .member(member)
                .scheduler(scheduler)
                .build());
    }

    /**
     * 모든 댓글 조회
     *
     * @param schedulerId 일정 id
     * @param pageable    페이지 값
     * @return Page에서 원하는 정보 값만 담은 List를 반환
     */
    public Page<Comment> getAllCommentsByScheduler(Long schedulerId, Pageable pageable) {
        return commentRepository.findAllBySchedulerIdOrderByModifiedAtDesc(schedulerId, pageable);
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
    public Comment updateComment(Long commentId, Long memberId, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentExceptionCode.COMMENT_NOT_FOUND));

        comment.validateMember(memberId);

        comment.updateContent(commentUpdateDto.getContent());

        return comment;
    }

    /**
     * 댓글 삭제
     *
     * @param commentId 댓글 id
     * @param memberId  유저 id
     * @return 삭제된 댓글 정보
     */
    public Comment deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentExceptionCode.COMMENT_NOT_FOUND));

        comment.validateMember(memberId);

        commentRepository.delete(comment);

        return comment;
    }

}
