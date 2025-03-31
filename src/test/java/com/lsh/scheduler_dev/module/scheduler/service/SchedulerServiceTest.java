package com.lsh.scheduler_dev.module.scheduler.service;

import com.lsh.scheduler_dev.common.response.ListResponse;
import com.lsh.scheduler_dev.common.utils.password.PasswordUtils;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.member.exception.MemberException;
import com.lsh.scheduler_dev.module.member.exception.MemberExceptionCode;
import com.lsh.scheduler_dev.module.member.service.MemberService;
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
import java.util.Objects;
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
    private MemberService memberService;

    @Mock
    private SchedulerRepository schedulerRepository;

    @InjectMocks
    private SchedulerService schedulerService;

    @Test
    @DisplayName("일정 생성 성공")
    void success_saveScheduler() {
        // Given
        Member member = getMember();
        SchedulerCreateDto schedulerCreateDto = new SchedulerCreateDto("test", "test");

        given(memberService.findById(anyLong()))
                .willReturn(member);
        given(schedulerRepository.save(any()))
                .willReturn(getScheduler());

        // When
        SchedulerDto schedulerDto = schedulerService.saveScheduler(1L, schedulerCreateDto);

        // Then
        verify(schedulerRepository, times(1)).save(any());
        assertAll(
                () -> assertEquals(schedulerDto.getTitle(), schedulerCreateDto.getTitle()),
                () -> assertEquals(schedulerDto.getContent(), schedulerCreateDto.getContent())
        );

    }

    @Test
    @DisplayName("일정 저장 실패 - 유저를 찾을 수 없음")
    void fail_saveScheduler_memberNotFound() {
        // Given
        SchedulerCreateDto schedulerCreateDto = new SchedulerCreateDto("test", "test");

        given(memberService.findById(anyLong()))
                .willThrow(new MemberException(MemberExceptionCode.MEMBER_NOT_FOUND));

        // When
        MemberException exception = assertThrows(MemberException.class,
                () -> schedulerService.saveScheduler(1L, schedulerCreateDto));

        // Then
        assertEquals(MemberExceptionCode.MEMBER_NOT_FOUND, exception.getErrorCode());

    }

    @Test
    @DisplayName("모든 일정 조회 성공")
    void success_getAllSchedulers() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Scheduler scheduler = getScheduler();
        List<Scheduler> list = List.of(scheduler, scheduler);

        given(schedulerRepository.findAllByOrderByModifiedAtDesc(any()))
                .willReturn(new PageImpl<>(list, pageable, list.size()));

        // When
        ListResponse<SchedulerDto> result = schedulerService.getAllSchedulers(pageable);

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
        SchedulerUpdateDto schedulerUpdateDto = new SchedulerUpdateDto("test2", "test2");

        given(schedulerRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(getScheduler()));

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
        SchedulerUpdateDto schedulerUpdateDto = new SchedulerUpdateDto("test2", "test2");

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
        Scheduler scheduler = getScheduler();
        Member member = getMember();

        given(schedulerRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(scheduler));

        // When
        SchedulerDto schedulerDto =
                schedulerService.deleteScheduler(member.getId(), Objects.requireNonNull(scheduler).getId());

        // Then
        assertAll(
                () -> assertEquals(schedulerDto.getSchedulerId(), scheduler.getId()),
                () -> assertEquals(schedulerDto.getName(), scheduler.getMember().getName()),
                () -> assertEquals(schedulerDto.getTitle(), scheduler.getTitle()),
                () -> assertEquals(schedulerDto.getContent(), scheduler.getContent())
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