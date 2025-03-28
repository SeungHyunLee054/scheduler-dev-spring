package com.lsh.scheduler_dev.module.comment.controller;

import com.lsh.scheduler_dev.common.constants.SessionConstants;
import com.lsh.scheduler_dev.common.response.ListResponse;
import com.lsh.scheduler_dev.module.comment.dto.request.CommentCreateDto;
import com.lsh.scheduler_dev.module.comment.dto.request.CommentUpdateDto;
import com.lsh.scheduler_dev.module.comment.dto.response.CommentDto;
import com.lsh.scheduler_dev.module.comment.service.CommentService;
import com.lsh.scheduler_dev.module.member.dto.MemberAuthDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{schedulerId}")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long schedulerId,
            @SessionAttribute(name = SessionConstants.AUTHORIZATION, required = false) MemberAuthDto memberAuthDto,
            @Valid @RequestBody CommentCreateDto dto
    ) {
        return ResponseEntity.ok(commentService.saveComment(schedulerId, memberAuthDto.getMemberId(), dto));
    }

    @GetMapping("/{schedulerId}")
    public ResponseEntity<ListResponse<CommentDto>> getComments(
            @PathVariable Long schedulerId,
            @RequestParam(defaultValue = "0") Integer pageIdx,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok(commentService.getAllComments(schedulerId, PageRequest.of(pageIdx, pageSize)));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @SessionAttribute(name = SessionConstants.AUTHORIZATION, required = false) MemberAuthDto memberAuthDto,
            @Valid @RequestBody CommentUpdateDto commentUpdateDto
    ) {
        return ResponseEntity.ok(commentService.updateComment(commentId, memberAuthDto.getMemberId(), commentUpdateDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentDto> deleteComment(
            @PathVariable Long commentId,
            @SessionAttribute(name = SessionConstants.AUTHORIZATION, required = false) MemberAuthDto memberAuthDto
    ) {
        return ResponseEntity.ok(commentService.deleteComment(commentId, memberAuthDto.getMemberId()));
    }

}