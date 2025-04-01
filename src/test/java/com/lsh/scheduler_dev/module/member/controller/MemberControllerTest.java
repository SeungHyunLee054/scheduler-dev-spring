package com.lsh.scheduler_dev.module.member.controller;

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
import com.lsh.scheduler_dev.common.constants.SessionConstants;
import com.lsh.scheduler_dev.common.constants.SessionExpiredConstant;
import com.lsh.scheduler_dev.common.response.ListResponse;
import com.lsh.scheduler_dev.module.member.dto.MemberAuthDto;
import com.lsh.scheduler_dev.module.member.dto.request.MemberCreateDto;
import com.lsh.scheduler_dev.module.member.dto.request.MemberSignInDto;
import com.lsh.scheduler_dev.module.member.dto.request.MemberUpdateDto;
import com.lsh.scheduler_dev.module.member.dto.response.MemberDto;
import com.lsh.scheduler_dev.module.member.service.MemberService;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {
	@MockitoBean
	private MemberService memberService;

	@MockitoBean
	private SessionExpiredConstant sessionExpiredConstant;

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

		when(memberDto.getMemberId())
			.thenReturn(1L);
		when(memberDto.getName())
			.thenReturn("test");
		when(memberDto.getEmail())
			.thenReturn("test@test");

		when(memberService.saveMember(any()))
			.thenReturn(memberDto);

		// When
		ResultActions perform = mockMvc.perform(post("/members/signup")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(memberCreateDto)));

		// Then
		perform.andDo(print())
			.andExpectAll(
				status().isCreated(),
				jsonPath("$.memberId")
					.value(1L),
				jsonPath("$.name")
					.value("test"),
				jsonPath("$.email")
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
				content().string("logged in")
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

		when(memberService.getAllMembers(any()))
			.thenReturn(ListResponse.<MemberDto>builder()
				.content(list)
				.build());

		// When
		ResultActions perform = mockMvc.perform(get("/members")
			.param("pageIdx", "0")
			.param("pageSize", "10"));

		// Then
		for (int i = 0; i < list.size(); i++) {
			perform.andDo(print())
				.andExpectAll(
					status().isOk(),
					jsonPath("$.content.[" + i + "].memberId")
						.value(memberDto.getMemberId()),
					jsonPath("$.content.[" + i + "].name")
						.value(memberDto.getName()),
					jsonPath("$.content.[" + i + "].email")
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

		when(memberService.updateMember(anyLong(), any()))
			.thenReturn(memberDto);

		// When
		ResultActions perform = mockMvc.perform(put("/members")
			.sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(memberUpdateDto)));

		// Then
		perform.andDo(print())
			.andExpectAll(
				status().isOk(),
				jsonPath("$.memberId")
					.value(1L),
				jsonPath("$.name")
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

		when(memberService.deleteMember(anyLong()))
			.thenReturn(memberDto);

		// When
		ResultActions perform = mockMvc.perform(delete("/members")
			.sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto)
			.contentType(MediaType.APPLICATION_JSON));

		// Then
		perform.andDo(print())
			.andExpectAll(
				status().isOk(),
				jsonPath("$.memberId")
					.value(1L),
				jsonPath("$.name")
					.value("test")
			);

	}
}
