package com.lsh.schedulerdev.domain.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
import com.lsh.schedulerdev.domain.member.entity.Member;
import com.lsh.schedulerdev.domain.member.service.MemberService;
import com.lsh.schedulerdev.domain.scheduler.entity.Scheduler;
import com.lsh.schedulerdev.domain.scheduler.service.SchedulerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final MemberService memberService;
	private final SchedulerService schedulerService;

	/**
	 * 댓글 저장
	 * @param schedulerId      일정 id
	 * @param memberId         유저 id
	 * @param commentCreateDto 작성하려는 댓글 내용
	 * @return 메세지, 댓글 정보
	 */
	@Transactional
	public CommonResponse<CommentDto> saveComment(Long schedulerId, Long memberId, CommentCreateDto commentCreateDto) {
		Member member = memberService.findById(memberId);
		Scheduler scheduler = schedulerService.findById(schedulerId);

		Comment comment = commentRepository.save(Comment.builder()
			.content(commentCreateDto.getContent())
			.member(member)
			.scheduler(scheduler)
			.build());

		scheduler.plusCommentCount();

		return CommonResponse
			.of(CommentSuccessCode.COMMENT_CREATE_SUCCESS, CommentDto.from(comment));
	}

	/**
	 * 모든 댓글 조회
	 * @param schedulerId 일정 id
	 * @param pageable    페이지 값
	 * @return 메세지, Page에서 원하는 정보 값만 담은 List를 반환
	 */
	public CommonResponses<CommentDto> getAllCommentsByScheduler(Long schedulerId, Pageable pageable) {
		Page<CommentDto> commentDtoPage =
			commentRepository.findAllBySchedulerIdOrderByModifiedAtDesc(schedulerId, pageable)
				.map(CommentDto::from);

		return CommonResponses.of(CommentSuccessCode.COMMENT_READ_ALL_SUCCESS, commentDtoPage);
	}

	/**
	 * 댓글 수정
	 * @param commentId        댓글 id
	 * @param memberId         유저 id
	 * @param commentUpdateDto 수정할 내용
	 * @return 메세지, 댓글 정보
	 */
	@Transactional
	public CommonResponse<CommentDto> updateComment(Long commentId, Long memberId, CommentUpdateDto commentUpdateDto) {
		Comment comment = findById(commentId);

		comment.validateMember(memberId);

		comment.updateContent(commentUpdateDto.getContent());

		return CommonResponse.of(CommentSuccessCode.COMMENT_UPDATE_SUCCESS, CommentDto.from(comment));
	}

	/**
	 * 댓글 삭제
	 * @param commentId 댓글 id
	 * @param memberId  유저 id
	 * @return 메세지, 삭제된 댓글 id
	 */
	@Transactional
	public CommonResponse<Long> deleteComment(Long commentId, Long memberId) {
		Comment comment = findById(commentId);

		comment.validateMember(memberId);

		Scheduler scheduler = comment.getScheduler();

		commentRepository.delete(comment);

		scheduler.minusCommentCount();

		return CommonResponse.of(CommentSuccessCode.COMMENT_DELETE_SUCCESS, comment.getId());
	}

	/**
	 * 댓글 조회
	 * @param commentId 댓글 id
	 * @return 메세지, id 값으로 조회된 댓글
	 */
	private Comment findById(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new CommentException(CommentExceptionCode.COMMENT_NOT_FOUND));
	}

}
