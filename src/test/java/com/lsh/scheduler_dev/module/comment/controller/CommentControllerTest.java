package com.lsh.scheduler_dev.module.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsh.scheduler_dev.common.constants.SessionConstants;
import com.lsh.scheduler_dev.common.response.ListResponse;
import com.lsh.scheduler_dev.module.comment.dto.request.CommentCreateDto;
import com.lsh.scheduler_dev.module.comment.dto.request.CommentUpdateDto;
import com.lsh.scheduler_dev.module.comment.dto.response.CommentDto;
import com.lsh.scheduler_dev.module.comment.facade.CommentFacade;
import com.lsh.scheduler_dev.module.comment.service.CommentService;
import com.lsh.scheduler_dev.module.member.dto.MemberAuthDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {CommentController.class})
class CommentControllerTest {
    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private CommentFacade commentFacade;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("댓글 생성 성공")
    void success_createComment() throws Exception {
        // Given
        CommentCreateDto commentCreateDto = new CommentCreateDto("test");
        MemberAuthDto memberAuthDto = getMemberAuthDto();
        CommentDto commentDto = getCommentDto();

        when(commentFacade.saveComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        // When
        ResultActions perform = mockMvc.perform(post("/comments/{schedulerId}", 1L)
                .sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentCreateDto)));

        // Then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content")
                                .value("test")
                );

    }

    @Test
    @DisplayName("해당 일정의 모든 댓글 조회 성공")
    void success_getAllCommentsByScheduler() throws Exception {
        // Given
        CommentDto commentDto = getCommentDto();
        List<CommentDto> list = List.of(commentDto);

        when(commentService.getAllCommentsByScheduler(anyLong(), any()))
                .thenReturn(ListResponse.<CommentDto>builder()
                        .content(list)
                        .build());

        // When
        ResultActions perform = mockMvc.perform(get("/comments/{schedulerId}", 1L)
                .param("pageIdx", "0")
                .param("pageSize", "10"));

        // Then
        for (int i = 0; i < list.size(); i++) {
            perform.andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.content.[" + i + "].commentId")
                                    .value(1L),
                            jsonPath("$.content.[" + i + "].content")
                                    .value("test")
                    );
        }

    }

    @Test
    @DisplayName("일정 수정 성공")
    void success_updateComment() throws Exception {
        // Given
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto("test2");
        MemberAuthDto memberAuthDto = getMemberAuthDto();

        when(commentService.updateComment(anyLong(), anyLong(), any()))
                .thenReturn(CommentDto.builder()
                        .content("test2")
                        .build());

        // When
        ResultActions perform = mockMvc.perform(put("/comments/{commentId}", 1L)
                .sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentUpdateDto)));

        // Then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content")
                                .value("test2")
                );

    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void success_deleteComment() throws Exception {
        // Given
        MemberAuthDto memberAuthDto = getMemberAuthDto();
        CommentDto commentDto = getCommentDto();

        when(commentFacade.deleteComment(anyLong(), anyLong()))
                .thenReturn(commentDto);

        // When
        ResultActions perform = mockMvc.perform(delete("/comments/{commentId}", 1L)
                .sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto));

        // Then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.commentId")
                                .value(commentDto.getCommentId())
                );

    }

    private MemberAuthDto getMemberAuthDto() {
        return MemberAuthDto.builder()
                .memberId(1L)
                .email("test@test")
                .build();
    }

    private CommentDto getCommentDto() {
        return CommentDto.builder()
                .commentId(1L)
                .schedulerId(1L)
                .memberId(1L)
                .content("test")
                .build();
    }

}