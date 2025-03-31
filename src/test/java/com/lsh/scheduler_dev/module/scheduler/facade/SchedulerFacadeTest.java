package com.lsh.scheduler_dev.module.scheduler.facade;

import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.member.exception.MemberException;
import com.lsh.scheduler_dev.module.member.exception.MemberExceptionCode;
import com.lsh.scheduler_dev.module.member.service.MemberService;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerCreateDto;
import com.lsh.scheduler_dev.module.scheduler.dto.response.SchedulerDto;
import com.lsh.scheduler_dev.module.scheduler.service.SchedulerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SchedulerFacadeTest {
    @Mock
    private SchedulerService schedulerService;

    @Mock
    private MemberService memberService;

    @Mock
    private SchedulerDto schedulerDto;

    @Mock
    private SchedulerCreateDto schedulerCreateDto;

    @Mock
    private Member member;

    @InjectMocks
    private SchedulerFacade schedulerFacade;

    @Test
    @DisplayName("일정 생성 성공")
    void success_saveScheduler() {
        // Given
        given(memberService.findById(anyLong()))
                .willReturn(member);
        given(schedulerService.saveScheduler(any(), any()))
                .willReturn(schedulerDto);

        // When
        schedulerFacade.saveScheduler(anyLong(), schedulerCreateDto);

        // Then
        verify(memberService, times(1)).findById(anyLong());
        verify(schedulerService, times(1)).saveScheduler(any(), any());

    }

    @Test
    @DisplayName("일정 저장 실패 - 유저를 찾을 수 없음")
    void fail_saveScheduler_memberNotFound() {
        // Given
        given(memberService.findById(anyLong()))
                .willThrow(new MemberException(MemberExceptionCode.MEMBER_NOT_FOUND));

        // When
        MemberException exception = assertThrows(MemberException.class,
                () -> schedulerFacade.saveScheduler(member.getId(), schedulerCreateDto));

        // Then
        assertEquals(MemberExceptionCode.MEMBER_NOT_FOUND, exception.getErrorCode());

    }

}