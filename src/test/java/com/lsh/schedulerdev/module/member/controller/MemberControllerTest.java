package com.lsh.schedulerdev.module.member.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsh.schedulerdev.common.constants.SessionConstants;
import com.lsh.schedulerdev.common.constants.SessionExpiredConstant;
import com.lsh.schedulerdev.common.response.CommonResponse;
import com.lsh.schedulerdev.common.response.CommonResponses;
import com.lsh.schedulerdev.module.member.dto.MemberAuthDto;
import com.lsh.schedulerdev.module.member.dto.request.MemberCreateDto;
import com.lsh.schedulerdev.module.member.dto.request.MemberSignInDto;
import com.lsh.schedulerdev.module.member.dto.request.MemberUpdateDto;
import com.lsh.schedulerdev.module.member.dto.response.MemberDto;
import com.lsh.schedulerdev.module.member.service.MemberService;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {
	@MockitoBean
	private MemberService memberService;

	@MockitoBean
	private SessionExpiredConstant sessionExpiredConstant;

	@Mock
	private CommonResponse<MemberDto> responseDto;

	@Mock
	private CommonResponse<Long> responseLong;

	@Mock
	private CommonResponses<MemberDto> responses;

	@Mock
	private MemberDto memberDto;

	@Mock
	private MemberCreateDto memberCreateDto;

	@Mock
	private MemberSignInDto memberSignInDto;

	@Mock
	private MemberUpdateDto memberUpdateDto;

	@Mock
	private MemberAuthDto memberAuthDto;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("회원 가입 성공")
	void success_signUp() throws Exception {
		// Given
		when(memberCreateDto.getName())
			.thenReturn("test");
		when(memberCreateDto.getEmail())
			.thenReturn("test@test.com");
		when(memberCreateDto.getPassword())
			.thenReturn("testtest");

		when(responseDto.getMessage())
			.thenReturn("회원가입 성공");
		when(responseDto.getResult())
			.thenReturn(memberDto);

		when(memberDto.getMemberId())
			.thenReturn(1L);
		when(memberDto.getName())
			.thenReturn("test");
		when(memberDto.getEmail())
			.thenReturn("test@test");

		when(memberService.saveMember(any()))
			.thenReturn(responseDto);

		// When
		ResultActions perform = mockMvc.perform(post("/members/signup")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(memberCreateDto)));

		// Then
		perform.andDo(print())
			.andExpectAll(
				status().isCreated(),
				jsonPath("$.message")
					.value(responseDto.getMessage()),
				jsonPath("$.result.memberId")
					.value(1L),
				jsonPath("$.result.name")
					.value("test"),
				jsonPath("$.result.email")
					.value("test@test")
			);

	}

	@Test
	@DisplayName("로그인 성공")
	void success_signIn() throws Exception {
		// Given
		MockHttpSession session = new MockHttpSession();

		when(memberSignInDto.getEmail())
			.thenReturn("test@test");
		when(memberSignInDto.getPassword())
			.thenReturn("testtest");

		when(memberAuthDto.getMemberId())
			.thenReturn(1L);
		when(memberAuthDto.getEmail())
			.thenReturn("test@test");

		when(responseDto.getMessage())
			.thenReturn("로그인 성공");

		when(memberService.signIn(any()))
			.thenReturn(memberAuthDto);

		// When
		ResultActions perform = mockMvc.perform(post("/members/signin")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(memberSignInDto))
			.session(session));

		// Then
		perform.andDo(print())
			.andExpectAll(
				status().isOk(),
				jsonPath("$.message")
					.value(responseDto.getMessage()),
				jsonPath("$.result")
					.value(session.getId())
			);

		MemberAuthDto result = (MemberAuthDto)session.getAttribute(SessionConstants.AUTHORIZATION);
		assertAll(
			() -> assertEquals(1L, Objects.requireNonNull(result).getMemberId()),
			() -> assertEquals("test@test", Objects.requireNonNull(result).getEmail())
		);

	}

	@Test
	@DisplayName("유저 전체 조회 성공")
	void success_getAllMembers() throws Exception {
		// Given
		List<MemberDto> list = List.of(memberDto, memberDto);

		when(responses.getMessage())
			.thenReturn("유저 전체 조회 성공");
		when(responses.getResult())
			.thenReturn(list);

		when(memberService.getAllMembers(any()))
			.thenReturn(responses);

		// When
		ResultActions perform = mockMvc.perform(get("/members")
			.param("pageIdx", "0")
			.param("pageSize", "10"));

		// Then
		for (int i = 0; i < list.size(); i++) {
			perform.andDo(print())
				.andExpectAll(
					status().isOk(),
					jsonPath("$.message")
						.value(responses.getMessage()),
					jsonPath("$.result.[" + i + "].memberId")
						.value(memberDto.getMemberId()),
					jsonPath("$.result.[" + i + "].name")
						.value(memberDto.getName()),
					jsonPath("$.result.[" + i + "].email")
						.value(memberDto.getEmail())
				);
		}

	}

	@Test
	@DisplayName("유저 수정 성공")
	void success_updateMember() throws Exception {
		// Given
		when(memberUpdateDto.getName())
			.thenReturn("t2");
		when(memberUpdateDto.getPassword())
			.thenReturn("testtest2");

		when(memberAuthDto.getMemberId())
			.thenReturn(1L);
		when(memberAuthDto.getEmail())
			.thenReturn("test@test");

		when(memberDto.getMemberId())
			.thenReturn(1L);
		when(memberDto.getName())
			.thenReturn("t2");

		when(responseDto.getMessage())
			.thenReturn("유저 수정 성공");
		when(responseDto.getResult())
			.thenReturn(memberDto);

		when(memberService.updateMember(anyLong(), any()))
			.thenReturn(responseDto);

		// When
		ResultActions perform = mockMvc.perform(put("/members")
			.sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(memberUpdateDto)));

		// Then
		perform.andDo(print())
			.andExpectAll(
				status().isOk(),
				jsonPath("$.message")
					.value(responseDto.getMessage()),
				jsonPath("$.result.memberId")
					.value(1L),
				jsonPath("$.result.name")
					.value("t2")
			);

	}

	@Test
	@DisplayName("유저 삭제 성공")
	void success_deleteMember() throws Exception {
		// Given
		when(memberAuthDto.getMemberId())
			.thenReturn(1L);
		when(memberAuthDto.getEmail())
			.thenReturn("test@test");

		when(memberDto.getMemberId())
			.thenReturn(1L);
		when(memberDto.getName())
			.thenReturn("test");

		when(responseLong.getMessage())
			.thenReturn("유저 삭제 성공");
		when(responseLong.getResult())
			.thenReturn(1L);

		when(memberService.deleteMember(anyLong()))
			.thenReturn(responseLong);

		// When
		ResultActions perform = mockMvc.perform(delete("/members")
			.sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto)
			.contentType(MediaType.APPLICATION_JSON));

		// Then
		perform.andDo(print())
			.andExpectAll(
				status().isOk(),
				jsonPath("$.message")
					.value(responseLong.getMessage()),
				jsonPath("$.result")
					.value(1L)
			);

	}
}
