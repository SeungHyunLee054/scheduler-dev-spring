package com.lsh.scheduler_dev.module.member.controller;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {
    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private SessionExpiredConstant sessionExpiredConstant;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("회원 가입 성공")
    void success_signUp() throws Exception {
        // Given
        MemberCreateDto memberCreateDto = new MemberCreateDto("test", "test@test", "testtest");

        when(memberService.saveMember(any()))
                .thenReturn(getMemberDto());

        // When
        ResultActions perform = mockMvc.perform(post("/members/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberCreateDto)));

        // Then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
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
        MemberSignInDto memberSignInDto = new MemberSignInDto("test@test", "testtest");
        MockHttpSession session = new MockHttpSession();
        MemberAuthDto memberAuthDto = getMemberAuthDto();

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

        MemberAuthDto result = (MemberAuthDto) session.getAttribute(SessionConstants.AUTHORIZATION);
        assertAll(
                () -> assertEquals(1L, Objects.requireNonNull(result).getMemberId()),
                () -> assertEquals("test@test", Objects.requireNonNull(result).getEmail())
        );

    }

    @Test
    @DisplayName("유저 전체 조회 성공")
    void success_getAllMembers() throws Exception {
        // Given
        MemberDto memberDto = getMemberDto();
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
                                    .value(1L),
                            jsonPath("$.content.[" + i + "].name")
                                    .value("test"),
                            jsonPath("$.content.[" + i + "].email")
                                    .value("test@test")
                    );
        }

    }

    @Test
    @DisplayName("유저 수정 성공")
    void success_updateMember() throws Exception {
        // Given
        MemberAuthDto memberAuthDto = getMemberAuthDto();
        MemberDto memberDto = getMemberDto();
        MemberUpdateDto memberUpdateDto = new MemberUpdateDto("test", "testtest");

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
                                .value("test")
                );

    }

    @Test
    @DisplayName("유저 삭제 성공")
    void success_deleteMember() throws Exception {
        // Given
        MemberAuthDto memberAuthDto = getMemberAuthDto();
        MemberDto memberDto = getMemberDto();

        when(memberService.deleteMember(anyLong()))
                .thenReturn(memberDto);

        // When
        ResultActions perform = mockMvc.perform(delete("/members/{memberId}", memberDto.getMemberId())
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

    private MemberAuthDto getMemberAuthDto() {
        return MemberAuthDto.builder()
                .memberId(1L)
                .email("test@test")
                .build();
    }

    private MemberDto getMemberDto() {
        LocalDateTime now = LocalDateTime.now();

        return MemberDto.builder()
                .memberId(1L)
                .name("test")
                .email("test@test")
                .createdAt(now)
                .modifiedAt(now)
                .build();
    }

}