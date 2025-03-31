package com.lsh.scheduler_dev.module.comment.facade;

import com.lsh.scheduler_dev.module.comment.dto.request.CommentCreateDto;
import com.lsh.scheduler_dev.module.comment.dto.response.CommentDto;
import com.lsh.scheduler_dev.module.comment.service.CommentService;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.member.exception.MemberException;
import com.lsh.scheduler_dev.module.member.exception.MemberExceptionCode;
import com.lsh.scheduler_dev.module.member.service.MemberService;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import com.lsh.scheduler_dev.module.scheduler.exception.SchedulerException;
import com.lsh.scheduler_dev.module.scheduler.exception.SchedulerExceptionCode;
import com.lsh.scheduler_dev.module.scheduler.service.SchedulerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentFacadeTest {
    @Mock
    private CommentService commentService;

    @Mock
    private MemberService memberService;

    @Mock
    private SchedulerService schedulerService;

    @Mock
    private Member member;

    @Mock
    private Scheduler scheduler;

    @Mock
    private CommentDto commentDto;

    @Mock
    private CommentCreateDto commentCreateDto;

    @InjectMocks
    private CommentFacade commentFacade;

    @Test
    @DisplayName("댓글 생성 성공")
    void success_saveComment() {
        // Given
        given(memberService.findById(anyLong()))
                .willReturn(member);
        given(schedulerService.findById(anyLong()))
                .willReturn(scheduler);
        given(commentService.saveComment(any(), any(), any()))
                .willReturn(commentDto);

        // When
        commentFacade.saveComment(1L, 1L, commentCreateDto);

        // Then
        verify(memberService, times(1)).findById(anyLong());
        verify(schedulerService, times(1)).plusCommentCount(any());
        verify(schedulerService, times(1)).findById(anyLong());
        verify(commentService, times(1)).saveComment(any(), any(), any());

    }

    @Test
    @DisplayName("댓글 생성 실패 - 유저를 찾을 수 없음")
    void fail_saveComment_memberNotFound() {
        // Given
        given(memberService.findById(anyLong()))
                .willThrow(new MemberException(MemberExceptionCode.MEMBER_NOT_FOUND));

        // When
        MemberException exception = assertThrows(MemberException.class,
                () -> commentFacade.saveComment(1L, 1L, commentCreateDto));

        // Then
        assertEquals(MemberExceptionCode.MEMBER_NOT_FOUND, exception.getErrorCode());

    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void success_deleteComment() {
        // Given
        given(commentService.deleteComment(anyLong(), anyLong()))
                .willReturn(commentDto);

        // When
        commentFacade.deleteComment(1L, 1L);

        // Then
        verify(commentService, times(1)).deleteComment(anyLong(), anyLong());
        verify(schedulerService, times(1)).findById(anyLong());
        verify(schedulerService, times(1)).minusCommentCount(any());

    }

    @Test
    @DisplayName("댓글 삭제 실패 - 일정을 찾을 수 없음")
    void fail_deleteComment_schedulerNotFound() {
        // Given
        given(commentService.deleteComment(anyLong(), anyLong()))
                .willReturn(commentDto);
        given(schedulerService.findById(anyLong()))
                .willThrow(new SchedulerException(SchedulerExceptionCode.SCHEDULER_NOT_FOUND));

        // When
        SchedulerException exception = assertThrows(SchedulerException.class,
                () -> commentFacade.deleteComment(1L, 1L));

        // Then
        assertEquals(SchedulerExceptionCode.SCHEDULER_NOT_FOUND, exception.getErrorCode());

    }

}