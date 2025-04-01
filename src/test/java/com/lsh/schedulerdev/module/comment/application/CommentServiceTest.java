package com.lsh.schedulerdev.module.comment.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lsh.schedulerdev.module.comment.domain.model.Comment;
import com.lsh.schedulerdev.module.comment.dto.request.CommentCreateDto;
import com.lsh.schedulerdev.module.comment.service.CommentDomainService;
import com.lsh.schedulerdev.module.member.domain.model.Member;
import com.lsh.schedulerdev.module.member.exception.MemberException;
import com.lsh.schedulerdev.module.member.exception.MemberExceptionCode;
import com.lsh.schedulerdev.module.member.service.MemberService;
import com.lsh.schedulerdev.module.scheduler.domain.model.Scheduler;
import com.lsh.schedulerdev.module.scheduler.exception.SchedulerException;
import com.lsh.schedulerdev.module.scheduler.exception.SchedulerExceptionCode;
import com.lsh.schedulerdev.module.scheduler.service.SchedulerDomainService;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
	@Mock
	private CommentDomainService commentDomainService;

	@Mock
	private MemberService memberService;

	@Mock
	private SchedulerDomainService schedulerDomainService;

	@Mock
	private Member member;

	@Mock
	private Scheduler scheduler;

	@Mock
	private Comment comment;

	@Mock
	private CommentCreateDto commentCreateDto;

	@InjectMocks
	private CommentService commentService;

	@Test
	@DisplayName("댓글 생성 성공")
	void success_saveComment() {
		// Given
		given(comment.getMember())
			.willReturn(member);
		given(comment.getScheduler())
			.willReturn(scheduler);

		given(member.getId())
			.willReturn(1L);

		given(memberService.findById(anyLong()))
			.willReturn(member);
		given(schedulerDomainService.findById(anyLong()))
			.willReturn(scheduler);
		given(commentDomainService.saveComment(any(), any(), any()))
			.willReturn(comment);

		// When
		commentService.saveComment(1L, 1L, commentCreateDto);

		// Then
		verify(memberService, times(1)).findById(anyLong());
		verify(schedulerDomainService, times(1)).plusCommentCount(any());
		verify(schedulerDomainService, times(1)).findById(anyLong());
		verify(commentDomainService, times(1)).saveComment(any(), any(), any());

	}

	@Test
	@DisplayName("댓글 생성 실패 - 유저를 찾을 수 없음")
	void fail_saveComment_memberNotFound() {
		// Given
		given(memberService.findById(anyLong()))
			.willThrow(new MemberException(MemberExceptionCode.MEMBER_NOT_FOUND));

		// When
		MemberException exception = assertThrows(MemberException.class,
			() -> commentService.saveComment(1L, 1L, commentCreateDto));

		// Then
		assertEquals(MemberExceptionCode.MEMBER_NOT_FOUND, exception.getErrorCode());

	}

	@Test
	@DisplayName("댓글 삭제 성공")
	void success_deleteComment() {
		// Given

		// When
		commentService.deleteComment(1L, 1L);

		// Then
		verify(commentDomainService, times(1)).deleteComment(anyLong(), anyLong());
		verify(schedulerDomainService, times(1)).findById(anyLong());
		verify(schedulerDomainService, times(1)).minusCommentCount(any());

	}

	@Test
	@DisplayName("댓글 삭제 실패 - 일정을 찾을 수 없음")
	void fail_deleteComment_schedulerNotFound() {
		// Given
		given(schedulerDomainService.findById(anyLong()))
			.willThrow(new SchedulerException(SchedulerExceptionCode.SCHEDULER_NOT_FOUND));

		// When
		SchedulerException exception = assertThrows(SchedulerException.class,
			() -> commentService.deleteComment(1L, 1L));

		// Then
		assertEquals(SchedulerExceptionCode.SCHEDULER_NOT_FOUND, exception.getErrorCode());

	}

}
