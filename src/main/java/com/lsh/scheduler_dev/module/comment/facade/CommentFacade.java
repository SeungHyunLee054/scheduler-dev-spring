package com.lsh.scheduler_dev.module.comment.facade;

import com.lsh.scheduler_dev.module.comment.dto.request.CommentCreateDto;
import com.lsh.scheduler_dev.module.comment.dto.response.CommentDto;
import com.lsh.scheduler_dev.module.comment.service.CommentService;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.member.service.MemberService;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import com.lsh.scheduler_dev.module.scheduler.service.SchedulerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentFacade {
    private final CommentService commentService;
    private final MemberService memberService;
    private final SchedulerService schedulerService;

    @Transactional
    public CommentDto saveComment(Long schedulerId, Long memberId, CommentCreateDto commentCreateDto) {
        Member member = memberService.findById(memberId);
        Scheduler scheduler = schedulerService.findById(schedulerId);

        CommentDto commentDto = commentService.saveComment(member, scheduler, commentCreateDto);

        schedulerService.plusCommentCount(scheduler);

        return commentDto;
    }

    @Transactional
    public CommentDto deleteComment(Long commentId, Long memberId) {
        CommentDto commentDto = commentService.deleteComment(commentId, memberId);

        Scheduler scheduler = schedulerService.findById(commentDto.getSchedulerId());

        schedulerService.minusCommentCount(scheduler);

        return commentDto;
    }

}