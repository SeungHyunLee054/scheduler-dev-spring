package com.lsh.scheduler_dev.module.scheduler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsh.scheduler_dev.common.constants.SessionConstants;
import com.lsh.scheduler_dev.common.response.ListResponse;
import com.lsh.scheduler_dev.module.member.dto.MemberAuthDto;
import com.lsh.scheduler_dev.module.scheduler.application.SchedulerService;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerUpdateDto;
import com.lsh.scheduler_dev.module.scheduler.dto.response.SchedulerDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

@WebMvcTest(controllers = SchedulerController.class)
class SchedulerControllerTest {
    @MockitoBean
    private SchedulerService schedulerService;

    @Mock
    private SchedulerUpdateDto schedulerUpdateDto;

    @Mock
    private SchedulerDto schedulerDto;

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

        when(schedulerService.saveScheduler(any(), any()))
                .thenReturn(schedulerDto);

        // When
        ResultActions perform = mockMvc.perform(post("/schedulers")
                .sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(schedulerDto)));

        // Then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.schedulerId")
                                .value(1L),
                        jsonPath("$.memberId")
                                .value(1L),
                        jsonPath("$.name")
                                .value("test"),
                        jsonPath("$.title")
                                .value("test"),
                        jsonPath("$.content")
                                .value("test"),
                        jsonPath("$.commentCount")
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

        when(schedulerService.getAllSchedulers(any()))
                .thenReturn(ListResponse.<SchedulerDto>builder()
                        .content(list)
                        .build());

        // When
        ResultActions perform = mockMvc.perform(get("/schedulers")
                .param("pageIdx", "0")
                .param("pageSize", "10"));

        // Then
        for (int i = 0; i < list.size(); i++) {
            perform.andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.content.[" + i + "].schedulerId")
                                    .value(1L),
                            jsonPath("$.content.[" + i + "].memberId")
                                    .value(1L),
                            jsonPath("$.content.[" + i + "].name")
                                    .value("test"),
                            jsonPath("$.content.[" + i + "].title")
                                    .value("test"),
                            jsonPath("$.content.[" + i + "].content")
                                    .value("test"),
                            jsonPath("$.content.[" + i + "].commentCount")
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

        when(schedulerService.updateScheduler(anyLong(), anyLong(), any()))
                .thenReturn(schedulerDto);

        // When
        ResultActions perform = mockMvc.perform(put("/schedulers/{schedulerId}", 1L)
                .sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(schedulerUpdateDto)));

        // Then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.title")
                                .value("test2"),
                        jsonPath("$.content")
                                .value("test2")
                );

    }

    @Test
    @DisplayName("일정 삭제 성공")
    void success_deleteScheduler() throws Exception {
        // Given
        when(schedulerService.deleteScheduler(anyLong(), anyLong()))
                .thenReturn(schedulerDto);

        // When
        ResultActions perform = mockMvc.perform(delete("/schedulers/{schedulerId}", 1L)
                .sessionAttr(SessionConstants.AUTHORIZATION, memberAuthDto));

        // Then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.schedulerId")
                                .value(schedulerDto.getSchedulerId()),
                        jsonPath("$.memberId")
                                .value(memberAuthDto.getMemberId())
                );

    }

}