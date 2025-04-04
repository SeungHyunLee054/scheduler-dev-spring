package com.lsh.schedulerdev.domain.comment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.lsh.schedulerdev.common.response.CommonResponse;
import com.lsh.schedulerdev.common.response.CommonResponses;
import com.lsh.schedulerdev.domain.comment.code.CommentExceptionCode;
import com.lsh.schedulerdev.domain.comment.code.CommentSuccessCode;
import com.lsh.schedulerdev.domain.comment.dto.request.CommentCreateDto;
import com.lsh.schedulerdev.domain.comment.dto.request.CommentUpdateDto;
import com.lsh.schedulerdev.domain.comment.dto.response.CommentDto;
import com.lsh.schedulerdev.domain.comment.entity.Comment;
import com.lsh.schedulerdev.domain.comment.exception.CommentException;
import com.lsh.schedulerdev.domain.comment.repository.CommentRepository;
import com.lsh.schedulerdev.domain.member.code.MemberExceptionCode;
import com.lsh.schedulerdev.domain.member.entity.Member;
import com.lsh.schedulerdev.domain.member.exception.MemberException;
import com.lsh.schedulerdev.domain.member.service.MemberService;
import com.lsh.schedulerdev.domain.scheduler.code.SchedulerExceptionCode;
import com.lsh.schedulerdev.domain.scheduler.entity.Scheduler;
import com.lsh.schedulerdev.domain.scheduler.exception.SchedulerException;
import com.lsh.schedulerdev.domain.scheduler.service.SchedulerService;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
	@Mock
	private CommentRepository commentRepository;

	@Mock
	private MemberService memberService;

	@Mock
	private SchedulerService schedulerService;

	@Mock
	private Member member;

	@Mock
	private Scheduler scheduler;

	@Mock
	private Comment comment;

	@Mock
	private CommentCreateDto commentCreateDto;

	@Mock
	private CommentUpdateDto commentUpdateDto;

	@InjectMocks
	private CommentService commentService;

	@Nested
	@DisplayName("댓글 생성 테스트")
	class CommentSaveTest {
		@Test
		@DisplayName("댓글 생성 성공")
		void success_saveComment() {
			// Given
			given(commentCreateDto.getContent())
				.willReturn("test");

			given(comment.getContent())
				.willReturn("test");

			given(memberService.findById(anyLong()))
				.willReturn(member);
			given(schedulerService.findById(anyLong()))
				.willReturn(scheduler);
			given(commentRepository.save(any()))
				.willReturn(comment);

			// When
			CommonResponse<CommentDto> response =
				commentService.saveComment(1L, 1L, commentCreateDto);

			// Then
			verify(memberService, times(1)).findById(anyLong());
			verify(schedulerService, times(1)).findById(anyLong());
			verify(commentRepository, times(1)).save(any());

			assertAll(
				() -> assertTrue(response.isSuccess()),
				() -> assertEquals(CommentSuccessCode.COMMENT_CREATE_SUCCESS.getMessage(), response.getMessage()),
				() -> assertEquals(CommentSuccessCode.COMMENT_CREATE_SUCCESS.getHttpStatus().value(),
					response.getStatus()),
				() -> assertEquals(comment.getId(), response.getResult().getCommentId()),
				() -> assertEquals(commentCreateDto.getContent(), response.getResult().getContent())
			);

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
		@DisplayName("댓글 생성 실패 - 일정을 찾을 수 없음")
		void fail_saveComment_schedulerNotFound() {
			// Given
			given(memberService.findById(anyLong()))
				.willReturn(member);
			given(schedulerService.findById(anyLong()))
				.willThrow(new SchedulerException(SchedulerExceptionCode.SCHEDULER_NOT_FOUND));

			// When
			SchedulerException exception = assertThrows(SchedulerException.class,
				() -> commentService.saveComment(1L, 1L, commentCreateDto));

			// Then
			assertEquals(SchedulerExceptionCode.SCHEDULER_NOT_FOUND, exception.getErrorCode());

		}
	}

	@Nested
	@DisplayName("댓글 조회 테스트")
	class GetCommentsTest {
		@Test
		@DisplayName("일정의 모든 댓글 조회 성공")
		void success_getAllCommentsByScheduler() {
			// Given
			Pageable pageable = PageRequest.of(0, 10);
			List<Comment> list = List.of(comment, comment);

			given(commentRepository.findAllBySchedulerIdOrderByModifiedAtDesc(anyLong(), any()))
				.willReturn(new PageImpl<>(list, pageable, list.size()));

			// When
			CommonResponses<CommentDto> responses = commentService.getAllCommentsByScheduler(1L, pageable);

			// Then
			List<CommentDto> result = responses.getResult();
			for (CommentDto commentDto : result) {
				assertAll(
					() -> assertTrue(responses.isSuccess()),
					() -> assertEquals(CommentSuccessCode.COMMENT_READ_ALL_SUCCESS.getMessage(),
						responses.getMessage()),
					() -> assertEquals(CommentSuccessCode.COMMENT_READ_ALL_SUCCESS.getHttpStatus().value(),
						responses.getStatus()),
					() -> assertEquals(comment.getId(), commentDto.getCommentId()),
					() -> assertEquals(comment.getContent(), commentDto.getContent())
				);
			}

		}
	}

	@Nested
	@DisplayName("댓글 수정 테스트")
	class UpdateCommentTest {
		@Test
		@DisplayName("댓글 수정 성공")
		void success_updateComment() {
			// Given
			given(commentUpdateDto.getContent())
				.willReturn("test2");

			given(comment.getContent())
				.willReturn("test2");

			given(commentRepository.findById(anyLong()))
				.willReturn(Optional.of(comment));

			// When
			CommonResponse<CommentDto> response =
				commentService.updateComment(1L, 1L, commentUpdateDto);

			// Then
			assertAll(
				() -> assertTrue(response.isSuccess()),
				() -> assertEquals(CommentSuccessCode.COMMENT_UPDATE_SUCCESS.getMessage(), response.getMessage()),
				() -> assertEquals(CommentSuccessCode.COMMENT_UPDATE_SUCCESS.getHttpStatus().value(),
					response.getStatus()),
				() -> assertEquals(commentUpdateDto.getContent(), response.getResult().getContent())
			);

		}

		@Test
		@DisplayName("댓글 수정 실패 - 조회한 댓글이 없음")
		void fail_updateComment_commentNotFound() {
			// Given
			given(commentRepository.findById(anyLong()))
				.willReturn(Optional.empty());

			// When
			CommentException exception = assertThrows(CommentException.class,
				() -> commentService.updateComment(1L, 1L, commentUpdateDto));

			// Then
			assertEquals(CommentExceptionCode.COMMENT_NOT_FOUND, exception.getErrorCode());

		}

		@Test
		@DisplayName("댓글 수정 실패 - 유저 불일치")
		void fail_updateComment_userMismatch() {
			// Given
			given(commentRepository.findById(anyLong()))
				.willReturn(Optional.of(Comment.builder()
					.id(1L)
					.member(Member.builder()
						.id(1L)
						.email("test@test")
						.password("testtest")
						.build())
					.content("test")
					.build()));

			// When
			CommentException exception = assertThrows(CommentException.class,
				() -> commentService.updateComment(1L, 2L, commentUpdateDto));

			// Then
			assertEquals(CommentExceptionCode.USER_MISMATCH, exception.getErrorCode());

		}
	}

	@Nested
	@DisplayName("댓글 삭제 테스트")
	class DeleteCommentTest {
		@Test
		@DisplayName("댓글 삭제 성공")
		void success_deleteComment() {
			// Given
			given(comment.getScheduler())
				.willReturn(scheduler);

			given(commentRepository.findById(anyLong()))
				.willReturn(Optional.of(comment));

			// When
			CommonResponse<Long> response = commentService.deleteComment(1L, 1L);

			// Then
			verify(commentRepository, times(1)).delete(any());
			verify(commentRepository, times(1)).findById(anyLong());

			assertAll(
				() -> assertTrue(response.isSuccess()),
				() -> assertEquals(CommentSuccessCode.COMMENT_DELETE_SUCCESS.getMessage(), response.getMessage()),
				() -> assertEquals(CommentSuccessCode.COMMENT_DELETE_SUCCESS.getHttpStatus().value(),
					response.getStatus()),
				() -> assertEquals(comment.getId(), response.getResult())
			);

		}

		@Test
		@DisplayName("댓글 삭제 실패 - 유저 불일치")
		void fail_deleteComment_userMismatch() {
			// Given
			given(commentRepository.findById(anyLong()))
				.willReturn(Optional.of(Comment.builder()
					.id(1L)
					.member(Member.builder()
						.id(1L)
						.email("test@test")
						.password("testtest")
						.build())
					.content("test")
					.build()));

			// When
			CommentException exception = assertThrows(CommentException.class,
				() -> commentService.deleteComment(1L, 2L));

			// Then
			assertEquals(CommentExceptionCode.USER_MISMATCH, exception.getErrorCode());

		}
	}

}
