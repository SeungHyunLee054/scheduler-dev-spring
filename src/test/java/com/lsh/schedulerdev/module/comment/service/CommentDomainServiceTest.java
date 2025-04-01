package com.lsh.schedulerdev.module.comment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.lsh.schedulerdev.module.comment.domain.model.Comment;
import com.lsh.schedulerdev.module.comment.dto.request.CommentCreateDto;
import com.lsh.schedulerdev.module.comment.dto.request.CommentUpdateDto;
import com.lsh.schedulerdev.module.comment.exception.CommentException;
import com.lsh.schedulerdev.module.comment.exception.CommentExceptionCode;
import com.lsh.schedulerdev.module.comment.repository.CommentRepository;
import com.lsh.schedulerdev.module.member.domain.model.Member;
import com.lsh.schedulerdev.module.scheduler.domain.model.Scheduler;

@ExtendWith(MockitoExtension.class)
class CommentDomainServiceTest {
	@Mock
	private CommentRepository commentRepository;

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
	private CommentDomainService commentDomainService;

	@Test
	@DisplayName("댓글 생성 성공")
	void success_saveComment() {
		// Given
		given(commentCreateDto.getContent())
			.willReturn("test");

		given(comment.getContent())
			.willReturn("test");

		given(commentRepository.save(any()))
			.willReturn(comment);

		// When
		Comment savedComment = commentDomainService.saveComment(member, scheduler, commentCreateDto);

		// Then
		verify(commentRepository, times(1)).save(any());
		assertAll(
			() -> assertEquals(comment.getId(), savedComment.getId()),
			() -> assertEquals(commentCreateDto.getContent(), savedComment.getContent())
		);

	}

	@Test
	@DisplayName("일정의 모든 댓글 조회 성공")
	void success_getAllCommentsByScheduler() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		List<Comment> list = List.of(comment, comment);

		given(commentRepository.findAllBySchedulerIdOrderByModifiedAtDesc(anyLong(), any()))
			.willReturn(new PageImpl<>(list, pageable, list.size()));

		// When
		Page<Comment> result = commentDomainService.getAllCommentsByScheduler(1L, pageable);

		// Then
		List<Comment> content = result.getContent();
		for (Comment c : content) {
			assertAll(
				() -> assertEquals(comment.getId(), c.getId()),
				() -> assertEquals(comment.getContent(), c.getContent())
			);
		}

	}

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
		Comment updatedComment = commentDomainService.updateComment(1L, 1L, commentUpdateDto);

		// Then
		assertAll(
			() -> assertEquals(commentUpdateDto.getContent(), updatedComment.getContent())
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
			() -> commentDomainService.updateComment(1L, 1L, commentUpdateDto));

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
			() -> commentDomainService.updateComment(1L, 2L, commentUpdateDto));

		// Then
		assertEquals(CommentExceptionCode.USER_MISMATCH, exception.getErrorCode());

	}

	@Test
	@DisplayName("댓글 삭제 성공")
	void success_deleteComment() {
		// Given
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.of(comment));

		// When
		Comment deletedComment = commentDomainService.deleteComment(1L, 1L);

		// Then
		assertAll(
			() -> assertEquals(comment.getId(), deletedComment.getId()),
			() -> assertEquals(comment.getContent(), deletedComment.getContent())
		);

	}

	@Test
	@DisplayName("댓글 삭제 실패 - 조회한 댓글이 없음")
	void fail_deleteComment_commentNotFound() {
		// Given
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// When
		CommentException exception = assertThrows(CommentException.class,
			() -> commentDomainService.deleteComment(1L, 1L));

		// Then
		assertEquals(CommentExceptionCode.COMMENT_NOT_FOUND, exception.getErrorCode());

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
			() -> commentDomainService.deleteComment(1L, 2L));

		// Then
		assertEquals(CommentExceptionCode.USER_MISMATCH, exception.getErrorCode());

	}

}
