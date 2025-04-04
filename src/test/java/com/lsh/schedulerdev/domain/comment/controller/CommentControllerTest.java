package com.lsh.schedulerdev.domain.comment.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsh.schedulerdev.common.constants.SessionConstants;
import com.lsh.schedulerdev.common.response.CommonResponse;
import com.lsh.schedulerdev.common.response.CommonResponses;
import com.lsh.schedulerdev.domain.comment.dto.request.CommentCreateDto;
import com.lsh.schedulerdev.domain.comment.dto.request.CommentUpdateDto;
import com.lsh.schedulerdev.domain.comment.dto.response.CommentDto;
import com.lsh.schedulerdev.domain.comment.service.CommentService;
import com.lsh.schedulerdev.domain.member.dto.MemberAuthDto;

@WebMvcTest(controllers = {CommentController.class})
class CommentControllerTest {
	@MockitoBean
	private CommentService commentService;

	@Mock
	private MemberAuthDto memberAuthDto;

	@Mock
	private CommentDto commentDto;

	@Mock
	private CommonResponse<CommentDto> responseDto;

	@Mock
	private CommonResponse<Long> responseLong;

	@Mock
	private CommonResponses<CommentDto> responses;

	@Mock
	private CommentCreateDto commentCreateDto;

	@Mock
	private CommentUpdateDto commentUpdateDto;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("댓글 생성 성공")
	void success_createComment() throws Exception {
		// Given
		when(commentDto.getCommentId())
			.thenReturn(1L);
		when(commentDto.getContent())
			.thenReturn("test");

		when(responseDto.getMessage())
			.thenReturn("댓글 생성 성공");
		when(responseDto.getResult())
			.thenReturn(commentDto);

		when(commentService.saveComment(anyLong(), anyLong(), any()))
			.thenReturn(responseDto);

		// When
		ResultActions perform = mockMvc.perform(post("/comments")
			.sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(commentCreateDto))
			.param("schedulerId", "1"));

		// Then
		perform.andDo(print())
			.andExpectAll(
				status().isCreated(),
				jsonPath("$.success")
					.value(responseDto.isSuccess()),
				jsonPath("$.status")
					.value(responseDto.getStatus()),
				jsonPath("$.message")
					.value(responseDto.getMessage()),
				jsonPath("$.result.commentId")
					.value(1L),
				jsonPath("$.result.content")
					.value("test")
			);

	}

	@Test
	@DisplayName("해당 일정의 모든 댓글 조회 성공")
	void success_getAllCommentsByScheduler() throws Exception {
		// Given
		List<CommentDto> list = List.of(commentDto, commentDto);

		when(commentDto.getCommentId())
			.thenReturn(1L);
		when(commentDto.getContent())
			.thenReturn("test");

		when(responses.getMessage())
			.thenReturn("댓글 전체 조회 성공");
		when(responses.getResult())
			.thenReturn(list);

		when(commentService.getAllCommentsByScheduler(anyLong(), any()))
			.thenReturn(responses);

		// When
		ResultActions perform = mockMvc.perform(get("/comments")
			.param("schedulerId", "1")
			.param("pageIdx", "0")
			.param("pageSize", "10"));

		// Then
		for (int i = 0; i < list.size(); i++) {
			perform.andDo(print())
				.andExpectAll(
					status().isOk(),
					jsonPath("$.success")
						.value(responseDto.isSuccess()),
					jsonPath("$.status")
						.value(responseDto.getStatus()),
					jsonPath("$.message")
						.value(responses.getMessage()),
					jsonPath("$.result.[" + i + "].commentId")
						.value(1L),
					jsonPath("$.result.[" + i + "].content")
						.value("test")
				);
		}

	}

	@Test
	@DisplayName("해당 일정의 해당 댓글 수정 성공")
	void success_updateComment() throws Exception {
		// Given
		when(commentDto.getContent())
			.thenReturn("test2");

		when(responseDto.getMessage())
			.thenReturn("일정 수정 성공");
		when(responseDto.getResult())
			.thenReturn(commentDto);

		when(commentService.updateComment(anyLong(), anyLong(), any()))
			.thenReturn(responseDto);

		// When
		ResultActions perform = mockMvc.perform(put("/comments/{commentId}", 1L)
			.sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(commentUpdateDto)));

		// Then
		perform.andDo(print())
			.andExpectAll(
				status().isOk(),
				jsonPath("$.success")
					.value(responseDto.isSuccess()),
				jsonPath("$.status")
					.value(responseDto.getStatus()),
				jsonPath("$.message")
					.value(responseDto.getMessage()),
				jsonPath("$.result.content")
					.value("test2")
			);

	}

	@Test
	@DisplayName("댓글 삭제 성공")
	void success_deleteComment() throws Exception {
		// Given
		when(commentDto.getCommentId())
			.thenReturn(1L);

		when(responseLong.getResult())
			.thenReturn(1L);
		when(responseLong.getMessage())
			.thenReturn("댓글 삭제 성공");

		when(commentService.deleteComment(anyLong(), anyLong()))
			.thenReturn(responseLong);

		// When
		ResultActions perform = mockMvc.perform(delete("/comments/{commentId}", 1L)
			.sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto));

		// Then
		perform.andDo(print())
			.andExpectAll(
				status().isOk(),
				jsonPath("$.success")
					.value(responseDto.isSuccess()),
				jsonPath("$.status")
					.value(responseDto.getStatus()),
				jsonPath("$.message")
					.value(responseLong.getMessage()),
				jsonPath("$.result")
					.value(commentDto.getCommentId())
			);

	}

}
