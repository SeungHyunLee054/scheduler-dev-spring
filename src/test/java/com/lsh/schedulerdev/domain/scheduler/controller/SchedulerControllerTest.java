package com.lsh.schedulerdev.domain.scheduler.controller;

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
import com.lsh.schedulerdev.domain.member.dto.MemberAuthDto;
import com.lsh.schedulerdev.domain.scheduler.dto.request.SchedulerUpdateDto;
import com.lsh.schedulerdev.domain.scheduler.dto.response.SchedulerDto;
import com.lsh.schedulerdev.domain.scheduler.service.SchedulerService;

@WebMvcTest(controllers = SchedulerController.class)
class SchedulerControllerTest {
	@MockitoBean
	private SchedulerService schedulerService;

	@Mock
	private SchedulerUpdateDto schedulerUpdateDto;

	@Mock
	private SchedulerDto schedulerDto;

	@Mock
	private CommonResponse<SchedulerDto> responseDto;

	@Mock
	private CommonResponse<Long> responseLong;

	@Mock
	private CommonResponses<SchedulerDto> responses;

	@Mock
	private MemberAuthDto memberAuthDto;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("일정 생성 성공")
	void success_createScheduler() throws Exception {
		// Given
		when(schedulerDto.getSchedulerId())
			.thenReturn(1L);
		when(schedulerDto.getMemberId())
			.thenReturn(1L);
		when(schedulerDto.getMemberId())
			.thenReturn(1L);
		when(schedulerDto.getName())
			.thenReturn("test");
		when(schedulerDto.getTitle())
			.thenReturn("test");
		when(schedulerDto.getContent())
			.thenReturn("test");

		when(responseDto.getMessage())
			.thenReturn("일정 생성 성공");
		when(responseDto.getResult())
			.thenReturn(schedulerDto);

		when(schedulerService.saveScheduler(any(), any()))
			.thenReturn(responseDto);

		// When
		ResultActions perform = mockMvc.perform(post("/schedulers")
			.sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(schedulerDto)));

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
				jsonPath("$.result.schedulerId")
					.value(1L),
				jsonPath("$.result.memberId")
					.value(1L),
				jsonPath("$.result.name")
					.value("test"),
				jsonPath("$.result.title")
					.value("test"),
				jsonPath("$.result.content")
					.value("test"),
				jsonPath("$.result.commentCount")
					.value(0)
			);

	}

	@Test
	@DisplayName("모든 일정 조회 성공")
	void success_getAllSchedulers() throws Exception {
		// Given
		when(schedulerDto.getSchedulerId())
			.thenReturn(1L);
		when(schedulerDto.getMemberId())
			.thenReturn(1L);
		when(schedulerDto.getName())
			.thenReturn("test");
		when(schedulerDto.getTitle())
			.thenReturn("test");
		when(schedulerDto.getContent())
			.thenReturn("test");

		List<SchedulerDto> list = List.of(schedulerDto, schedulerDto);

		when(responses.getMessage())
			.thenReturn("일정 전체 조회 성공");
		when(responses.getResult())
			.thenReturn(list);

		when(schedulerService.getAllSchedulers(any()))
			.thenReturn(responses);

		// When
		ResultActions perform = mockMvc.perform(get("/schedulers")
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
					jsonPath("$.result.[" + i + "].schedulerId")
						.value(1L),
					jsonPath("$.result.[" + i + "].memberId")
						.value(1L),
					jsonPath("$.result.[" + i + "].name")
						.value("test"),
					jsonPath("$.result.[" + i + "].title")
						.value("test"),
					jsonPath("$.result.[" + i + "].content")
						.value("test"),
					jsonPath("$.result.[" + i + "].commentCount")
						.value(0)

				);
		}

	}

	@Test
	@DisplayName("일정 수정 성공")
	void success_updateScheduler() throws Exception {
		// Given
		when(schedulerUpdateDto.getTitle())
			.thenReturn("test2");
		when(schedulerUpdateDto.getContent())
			.thenReturn("test2");

		when(schedulerDto.getTitle())
			.thenReturn("test2");
		when(schedulerDto.getContent())
			.thenReturn("test2");

		when(responseDto.getMessage())
			.thenReturn("일정 수정 성공");
		when(responseDto.getResult())
			.thenReturn(schedulerDto);

		when(schedulerService.updateScheduler(anyLong(), anyLong(), any()))
			.thenReturn(responseDto);

		// When
		ResultActions perform = mockMvc.perform(put("/schedulers/{schedulerId}", 1L)
			.sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(schedulerUpdateDto)));

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
				jsonPath("$.result.title")
					.value("test2"),
				jsonPath("$.result.content")
					.value("test2")
			);

	}

	@Test
	@DisplayName("일정 삭제 성공")
	void success_deleteScheduler() throws Exception {
		// Given
		when(responseLong.getMessage())
			.thenReturn("일정 삭제 성공");
		when(responseLong.getResult())
			.thenReturn(1L);

		when(schedulerService.deleteScheduler(anyLong(), anyLong()))
			.thenReturn(responseLong);

		// When
		ResultActions perform = mockMvc.perform(delete("/schedulers/{schedulerId}", 1L)
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
					.value(responseLong.getResult())
			);

	}

}
