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
import com.lsh.scheduler_dev.module.member.service.MemberService;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import com.lsh.scheduler_dev.module.scheduler.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final SchedulerService schedulerService;

    public CommentDto saveComment(Long schedulerId, Long memberId, CommentCreateDto commentCreateDto) {
        Member member = memberService.findById(memberId);
        Scheduler scheduler = schedulerService.findById(schedulerId);

        Comment savedComment = commentRepository.save(Comment.builder()
                .content(commentCreateDto.getContent())
                .member(member)
                .scheduler(scheduler)
                .build());

        schedulerService.plusCommentCount(scheduler);

        return CommentDto.toDto(savedComment);
    }

    public ListResponse<CommentDto> getAllComments(Long schedulerId, Pageable pageable) {
        return ListResponse
                .toListResponse(commentRepository.findAllBySchedulerIdOrderByModifiedAtDesc(schedulerId, pageable)
                        .map(CommentDto::toDto));
    }

    public CommentDto updateComment(Long commentId, Long memberId, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findById(commentId)
                .filter(c -> c.getMember().getId().equals(memberId))
                .orElseThrow(() -> new CommentException(CommentExceptionCode.USER_MISMATCH));

        comment.updateContent(commentUpdateDto.getContent());

        Comment savedComment = commentRepository.save(comment);

        return CommentDto.toDto(savedComment);
    }

    public CommentDto deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .filter(c -> c.getMember().getId().equals(memberId))
                .orElseThrow(() -> new CommentException(CommentExceptionCode.USER_MISMATCH));

        commentRepository.delete(comment);

        schedulerService.minusCommentCount(comment.getScheduler());

        return CommentDto.toDto(comment);
    }

}
