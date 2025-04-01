package com.lsh.scheduler_dev.module.scheduler.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.member.exception.MemberException;
import com.lsh.scheduler_dev.module.member.exception.MemberExceptionCode;
import com.lsh.scheduler_dev.module.member.service.MemberService;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerCreateDto;
import com.lsh.scheduler_dev.module.scheduler.service.SchedulerDomainService;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {
	@Mock
	private SchedulerDomainService schedulerDomainService;

	@Mock
	private MemberService memberService;

	@Mock
	private Scheduler scheduler;

	@Mock
	private SchedulerCreateDto schedulerCreateDto;

	@Mock
	private Member member;

	@InjectMocks
	private SchedulerService schedulerService;

	@Test
	@DisplayName("일정 생성 성공")
	void success_saveScheduler() {
		// Given
		given(scheduler.getMember())
			.willReturn(member);
		given(memberService.findById(anyLong()))
			.willReturn(member);
		given(schedulerDomainService.saveScheduler(any(), any()))
			.willReturn(scheduler);

		// When
		schedulerService.saveScheduler(anyLong(), schedulerCreateDto);

		// Then
		verify(memberService, times(1)).findById(anyLong());
		verify(schedulerDomainService, times(1)).saveScheduler(any(), any());

	}

	@Test
	@DisplayName("일정 저장 실패 - 유저를 찾을 수 없음")
	void fail_saveScheduler_memberNotFound() {
		// Given
		given(memberService.findById(anyLong()))
			.willThrow(new MemberException(MemberExceptionCode.MEMBER_NOT_FOUND));

		// When
		MemberException exception = assertThrows(MemberException.class,
			() -> schedulerService.saveScheduler(member.getId(), schedulerCreateDto));

		// Then
		assertEquals(MemberExceptionCode.MEMBER_NOT_FOUND, exception.getErrorCode());

	}

}
