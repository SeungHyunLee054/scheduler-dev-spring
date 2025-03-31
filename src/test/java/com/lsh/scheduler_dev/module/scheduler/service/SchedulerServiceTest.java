package com.lsh.scheduler_dev.module.scheduler.service;

import com.lsh.scheduler_dev.common.response.ListResponse;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerCreateDto;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerUpdateDto;
import com.lsh.scheduler_dev.module.scheduler.dto.response.SchedulerDto;
import com.lsh.scheduler_dev.module.scheduler.exception.SchedulerException;
import com.lsh.scheduler_dev.module.scheduler.exception.SchedulerExceptionCode;
import com.lsh.scheduler_dev.module.scheduler.repository.SchedulerRepository;
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
class SchedulerServiceTest {
    @Mock
    private SchedulerRepository schedulerRepository;

    @Mock
    private SchedulerCreateDto schedulerCreateDto;

    @Mock
    private SchedulerUpdateDto schedulerUpdateDto;

    @Mock
    private Member member;

    @Mock
    private Scheduler scheduler;

    @InjectMocks
    private SchedulerService schedulerService;

    @Test
    @DisplayName("일정 생성 성공")
    void success_saveScheduler() {
        // Given
        given(scheduler.getTitle())
                .willReturn("test");
        given(scheduler.getContent())
                .willReturn("test");
        given(scheduler.getMember())
                .willReturn(member);

        given(schedulerRepository.save(any()))
                .willReturn(scheduler);

        // When
        SchedulerDto schedulerDto = schedulerService.saveScheduler(member, schedulerCreateDto);

        // Then
        verify(schedulerRepository, times(1)).save(any());
        assertAll(
                () -> assertEquals("test", schedulerDto.getTitle()),
                () -> assertEquals("test", schedulerDto.getContent())
        );

    }

    @Test
    @DisplayName("모든 일정 조회 성공")
    void success_getAllSchedulers() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Scheduler> list = List.of(scheduler, scheduler);

        given(scheduler.getMember())
                .willReturn(member);
        given(schedulerRepository.findAllByOrderByModifiedAtDesc(any()))
                .willReturn(new PageImpl<>(list, pageable, list.size()));

        // When
        ListResponse<SchedulerDto> result = schedulerService.getAllSchedulers(any());

        // Then
        List<SchedulerDto> content = result.getContent();
        for (SchedulerDto schedulerDto : content) {
            assertAll(
                    () -> assertEquals(schedulerDto.getSchedulerId(), scheduler.getId()),
                    () -> assertEquals(schedulerDto.getTitle(), scheduler.getTitle()),
                    () -> assertEquals(schedulerDto.getContent(), scheduler.getContent())
            );
        }

    }

    @Test
    @DisplayName("일정 수정 성공")
    void success_updateScheduler() {
        // Given
        given(schedulerUpdateDto.getTitle())
                .willReturn("test2");
        given(schedulerUpdateDto.getContent())
                .willReturn("test2");

        given(member.getId())
                .willReturn(1L);

        given(scheduler.getMember())
                .willReturn(member);
        given(scheduler.getTitle())
                .willReturn("test2");
        given(scheduler.getContent())
                .willReturn("test2");

        given(schedulerRepository.findById(anyLong()))
                .willReturn(Optional.of(scheduler));


        // When
        SchedulerDto schedulerDto = schedulerService.updateScheduler(1L, 1L, schedulerUpdateDto);

        // Then
        assertAll(
                () -> assertEquals(schedulerDto.getTitle(), schedulerUpdateDto.getTitle()),
                () -> assertEquals(schedulerDto.getContent(), schedulerUpdateDto.getContent())
        );

    }

    @Test
    @DisplayName("일정 수정 실패 - 유저 불일치")
    void fail_updateScheduler_userMismatch() {
        // Given
        given(schedulerRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // When
        SchedulerException exception = assertThrows(SchedulerException.class,
                () -> schedulerService.updateScheduler(1L, 1L, schedulerUpdateDto));

        // Then
        assertEquals(SchedulerExceptionCode.USER_MISMATCH, exception.getErrorCode());

    }

    @Test
    @DisplayName("일정 삭제 성공")
    void success_deleteScheduler() {
        // Given
        given(member.getId())
                .willReturn(1L);

        given(scheduler.getId())
                .willReturn(1L);
        given(scheduler.getMember())
                .willReturn(member);

        given(schedulerRepository.findById(anyLong()))
                .willReturn(Optional.of(scheduler));

        // When
        SchedulerDto schedulerDto =
                schedulerService.deleteScheduler(1L, 1L);

        // Then
        assertAll(
                () -> assertEquals(scheduler.getId(), schedulerDto.getSchedulerId()),
                () -> assertEquals(scheduler.getMember().getName(), schedulerDto.getName()),
                () -> assertEquals(scheduler.getTitle(), schedulerDto.getTitle()),
                () -> assertEquals(scheduler.getContent(), schedulerDto.getContent())
        );

    }

    @Test
    @DisplayName("일정 삭제 실패 - 유저 불일치")
    void fail_deleteScheduler_userMismatch() {
        // Given
        given(schedulerRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // When
        SchedulerException exception = assertThrows(SchedulerException.class,
                () -> schedulerService.deleteScheduler(1L, 1L));

        // Then
        assertEquals(SchedulerExceptionCode.USER_MISMATCH, exception.getErrorCode());

    }

}