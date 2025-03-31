package com.lsh.scheduler_dev.module.scheduler.facade;

import com.lsh.scheduler_dev.common.utils.password.PasswordUtils;
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

import static org.junit.jupiter.api.Assertions.*;
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

    @InjectMocks
    private SchedulerFacade schedulerFacade;

    @Test
    @DisplayName("일정 생성 성공")
    void success_saveScheduler() {
        // Given
        Member member = getMember();
        SchedulerCreateDto schedulerCreateDto = new SchedulerCreateDto("test", "test");

        given(memberService.findById(anyLong()))
                .willReturn(member);
        given(schedulerService.saveScheduler(any(), any()))
                .willReturn(SchedulerDto.builder()
                        .schedulerId(1L)
                        .memberId(member.getId())
                        .name(member.getName())
                        .title(schedulerCreateDto.getTitle())
                        .content(schedulerCreateDto.getContent())
                        .build());

        // When
        schedulerFacade.saveScheduler(member.getId(), schedulerCreateDto);

        // Then
        verify(memberService,times(1)).findById(anyLong());
        verify(schedulerService,times(1)).saveScheduler(any(),any());

    }

    @Test
    @DisplayName("일정 저장 실패 - 유저를 찾을 수 없음")
    void fail_saveScheduler_memberNotFound() {
        // Given
        SchedulerCreateDto schedulerCreateDto = new SchedulerCreateDto("test", "test");
        Member member = getMember();

        given(memberService.findById(anyLong()))
                .willThrow(new MemberException(MemberExceptionCode.MEMBER_NOT_FOUND));

        // When
        MemberException exception = assertThrows(MemberException.class,
                () -> schedulerFacade.saveScheduler(member.getId(), schedulerCreateDto));

        // Then
        assertEquals(MemberExceptionCode.MEMBER_NOT_FOUND, exception.getErrorCode());

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