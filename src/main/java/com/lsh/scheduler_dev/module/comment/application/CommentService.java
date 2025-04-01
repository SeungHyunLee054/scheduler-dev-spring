package com.lsh.scheduler_dev.module.comment.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lsh.scheduler_dev.common.response.CommonResponse;
import com.lsh.scheduler_dev.common.response.CommonResponses;
import com.lsh.scheduler_dev.module.comment.domain.model.Comment;
import com.lsh.scheduler_dev.module.comment.dto.request.CommentCreateDto;
import com.lsh.scheduler_dev.module.comment.dto.request.CommentUpdateDto;
import com.lsh.scheduler_dev.module.comment.dto.response.CommentDto;
import com.lsh.scheduler_dev.module.comment.service.CommentDomainService;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.member.service.MemberService;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import com.lsh.scheduler_dev.module.scheduler.service.SchedulerDomainService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentDomainService commentDomainService;
	private final MemberService memberService;
	private final SchedulerDomainService schedulerDomainService;

	/**
	 * 댓글 저장
	 *
	 * @param schedulerId      일정 id
	 * @param memberId         유저 id
	 * @param commentCreateDto 작성하려는 댓글 내용
	 * @return 댓글 정보
	 */
	@Transactional
	public CommonResponse<CommentDto> saveComment(Long schedulerId, Long memberId, CommentCreateDto commentCreateDto) {
		Member member = memberService.findById(memberId);
		Scheduler scheduler = schedulerDomainService.findById(schedulerId);

		Comment comment = commentDomainService.saveComment(member, scheduler, commentCreateDto);

		schedulerDomainService.plusCommentCount(scheduler);

		CommentDto commentDto = CommentDto.from(comment);

		return CommonResponse.of("해당 일정에 댓글 생성 성공", commentDto);
	}

	/**
	 * 모든 댓글 조회
	 *
	 * @param schedulerId 일정 id
	 * @param pageable    페이지 값
	 * @return Page에서 원하는 정보 값만 담은 List를 반환
	 */
	public CommonResponses<CommentDto> getAllCommentsByScheduler(Long schedulerId, Pageable pageable) {
		Page<Comment> commentPage = commentDomainService.getAllCommentsByScheduler(schedulerId, pageable);
		Page<CommentDto> commentDtoPage = commentPage.map(CommentDto::from);

		return CommonResponses.from("해당 일정의 모든 댓글 조회 성공", commentDtoPage);
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
	public CommonResponse<CommentDto> updateComment(Long commentId, Long memberId, CommentUpdateDto commentUpdateDto) {
		Comment updateComment = commentDomainService.updateComment(commentId, memberId, commentUpdateDto);

		CommentDto commentDto = CommentDto.from(updateComment);

		return CommonResponse.of("해당 일정의 해당 댓글 수정 성공", commentDto);
	}

	/**
	 * 댓글 삭제
	 *
	 * @param commentId 댓글 id
	 * @param memberId  유저 id
	 * @return 삭제된 댓글 정보
	 */
	@Transactional
	public CommonResponse<CommentDto> deleteComment(Long commentId, Long memberId) {
		Comment comment = commentDomainService.deleteComment(commentId, memberId);

		Scheduler scheduler = schedulerDomainService.findById(comment.getScheduler().getId());

		schedulerDomainService.minusCommentCount(scheduler);

		CommentDto commentDto = CommentDto.from(comment);

		return CommonResponse.of("해당 일정의 해당 댓글 삭제 성공", commentDto);
	}

}
