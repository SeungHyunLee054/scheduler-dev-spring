package com.lsh.schedulerdev.domain.comment.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.lsh.schedulerdev.common.constants.SessionConstants;
import com.lsh.schedulerdev.common.response.CommonResponse;
import com.lsh.schedulerdev.common.response.CommonResponses;
import com.lsh.schedulerdev.domain.comment.dto.request.CommentCreateDto;
import com.lsh.schedulerdev.domain.comment.dto.request.CommentUpdateDto;
import com.lsh.schedulerdev.domain.comment.dto.response.CommentDto;
import com.lsh.schedulerdev.domain.comment.service.CommentService;
import com.lsh.schedulerdev.domain.member.dto.MemberAuthDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<CommonResponse<CommentDto>> createComment(
		@RequestParam Long schedulerId,
		@SessionAttribute(name = SessionConstants.AUTHORIZATION) MemberAuthDto memberAuthDto,
		@Valid @RequestBody CommentCreateDto commentCreateDto
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(commentService.saveComment(schedulerId, memberAuthDto.getMemberId(), commentCreateDto));
	}

	@GetMapping
	public ResponseEntity<CommonResponses<CommentDto>> getComments(
		@RequestParam Long schedulerId,
		@RequestParam(defaultValue = "0") Integer pageIdx,
		@RequestParam(defaultValue = "10") Integer pageSize
	) {
		return ResponseEntity.ok(
			commentService.getAllCommentsByScheduler(schedulerId, PageRequest.of(pageIdx, pageSize)));
	}

	@PutMapping("/{commentId}")
	public ResponseEntity<CommonResponse<CommentDto>> updateComment(
		@PathVariable Long commentId,
		@SessionAttribute(name = SessionConstants.AUTHORIZATION) MemberAuthDto memberAuthDto,
		@Valid @RequestBody CommentUpdateDto commentUpdateDto
	) {
		return ResponseEntity.ok(commentService
			.updateComment(commentId, memberAuthDto.getMemberId(), commentUpdateDto));
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<CommonResponse<Long>> deleteComment(
		@PathVariable Long commentId,
		@SessionAttribute(name = SessionConstants.AUTHORIZATION) MemberAuthDto memberAuthDto
	) {
		return ResponseEntity.ok(commentService.deleteComment(commentId, memberAuthDto.getMemberId()));
	}

}
