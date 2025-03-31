package com.lsh.scheduler_dev.module.comment.service;

import com.lsh.scheduler_dev.common.response.ListResponse;
import com.lsh.scheduler_dev.common.utils.password.PasswordUtils;
import com.lsh.scheduler_dev.module.comment.domain.model.Comment;
import com.lsh.scheduler_dev.module.comment.dto.request.CommentCreateDto;
import com.lsh.scheduler_dev.module.comment.dto.request.CommentUpdateDto;
import com.lsh.scheduler_dev.module.comment.dto.response.CommentDto;
import com.lsh.scheduler_dev.module.comment.exception.CommentException;
import com.lsh.scheduler_dev.module.comment.exception.CommentExceptionCode;
import com.lsh.scheduler_dev.module.comment.repository.CommentRepository;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("댓글 생성 성공")
    void success_saveComment() {
        // Given
        Member member = getMember();
        Scheduler scheduler = getScheduler();
        Comment comment = getComment();
        CommentCreateDto commentCreateDto = new CommentCreateDto("test");

        given(commentRepository.save(any()))
                .willReturn(comment);

        // When
        CommentDto commentDto = commentService.saveComment(member, scheduler, commentCreateDto);

        // Then
        verify(commentRepository, times(1)).save(any());
        assertAll(
                () -> assertEquals(comment.getId(), commentDto.getCommentId()),
                () -> assertEquals(commentCreateDto.getContent(), commentDto.getContent())
        );

    }

    @Test
    @DisplayName("일정의 모든 댓글 조회 성공")
    void success_getAllCommentsByScheduler() {
        // Given
        Comment comment = getComment();
        Pageable pageable = PageRequest.of(0, 10);
        List<Comment> list = List.of(comment, comment);

        given(commentRepository.findAllBySchedulerIdOrderByModifiedAtDesc(anyLong(), any()))
                .willReturn(new PageImpl<>(list, pageable, list.size()));

        // When
        ListResponse<CommentDto> result = commentService.getAllCommentsByScheduler(1L, pageable);

        // Then
        List<CommentDto> content = result.getContent();
        for (CommentDto commentDto : content) {
            assertAll(
                    () -> assertEquals(comment.getId(), commentDto.getCommentId()),
                    () -> assertEquals(comment.getContent(), commentDto.getContent())
            );
        }

    }

    @Test
    @DisplayName("댓글 수정 성공")
    void success_updateComment() {
        // Given
        Comment comment = getComment();
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto("test2");

        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(comment));

        // When
        CommentDto commentDto = commentService.updateComment(1L, 1L, commentUpdateDto);

        // Then
        assertAll(
                () -> assertEquals(commentUpdateDto.getContent(), commentDto.getContent())
        );

    }

    @Test
    @DisplayName("댓글 수정 실패 - 유저 불일치")
    void fail_updateComment_userMismatch() {
        // Given
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto("test2");

        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // When
        CommentException exception = assertThrows(CommentException.class,
                () -> commentService.updateComment(1L, 1L, commentUpdateDto));

        // Then
        assertEquals(CommentExceptionCode.USER_MISMATCH, exception.getErrorCode());

    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void success_deleteComment() {
        // Given
        Comment comment = getComment();

        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.of(comment));

        // When
        CommentDto commentDto = commentService.deleteComment(1L, 1L);

        // Then
        assertAll(
                () -> assertEquals(comment.getId(), commentDto.getCommentId()),
                () -> assertEquals(comment.getContent(), commentDto.getContent())
        );

    }

    @Test
    @DisplayName("댓글 삭제 실패 - 유저 불일치")
    void fail_deleteComment_userMismatch() {
        // Given
        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // When
        CommentException exception = assertThrows(CommentException.class,
                () -> commentService.deleteComment(1L, 1L));

        // Then
        assertEquals(CommentExceptionCode.USER_MISMATCH, exception.getErrorCode());

    }


    private Comment getComment() {
        return Comment.builder()
                .id(1L)
                .member(getMember())
                .scheduler(getScheduler())
                .content("test")
                .build();
    }

    private Scheduler getScheduler() {
        return Scheduler.builder()
                .id(1L)
                .title("test")
                .content("test")
                .member(getMember())
                .build();
    }

    private Member getMember() {
        return Member.builder()
                .id(1L)
                .name("test")
                .email("test@test")
                .password(PasswordUtils.encryptPassword("testtest"))
                .build();
    }

}