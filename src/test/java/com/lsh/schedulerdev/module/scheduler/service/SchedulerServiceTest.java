package com.lsh.schedulerdev.module.scheduler.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.lsh.schedulerdev.common.response.CommonResponse;
import com.lsh.schedulerdev.common.response.CommonResponses;
import com.lsh.schedulerdev.module.member.domain.model.Member;
import com.lsh.schedulerdev.module.member.exception.MemberException;
import com.lsh.schedulerdev.module.member.exception.MemberExceptionCode;
import com.lsh.schedulerdev.module.member.service.MemberService;
import com.lsh.schedulerdev.module.scheduler.domain.model.Scheduler;
import com.lsh.schedulerdev.module.scheduler.dto.request.SchedulerCreateDto;
import com.lsh.schedulerdev.module.scheduler.dto.request.SchedulerUpdateDto;
import com.lsh.schedulerdev.module.scheduler.dto.response.SchedulerDto;
import com.lsh.schedulerdev.module.scheduler.exception.SchedulerException;
import com.lsh.schedulerdev.module.scheduler.exception.SchedulerExceptionCode;
import com.lsh.schedulerdev.module.scheduler.repository.SchedulerRepository;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {
	@Mock
	private SchedulerRepository schedulerRepository;

	@Mock
	private MemberService memberService;

	@Mock
	private Scheduler scheduler;

	@Mock
	private Member member;

	@Mock
	private SchedulerCreateDto schedulerCreateDto;

	@Mock
	private SchedulerUpdateDto schedulerUpdateDto;

	@InjectMocks
	private SchedulerService schedulerService;

	@Test
	@DisplayName("일정 생성 성공")
	void success_saveScheduler() {
		// Given
		given(schedulerCreateDto.getTitle())
			.willReturn("test");
		given(schedulerCreateDto.getContent())
			.willReturn("test");

		given(scheduler.getTitle())
			.willReturn("test");
		given(scheduler.getContent())
			.willReturn("test");

		given(memberService.findById(anyLong()))
			.willReturn(member);
		given(schedulerRepository.save(any()))
			.willReturn(scheduler);

		// When
		CommonResponse<SchedulerDto> response = schedulerService.saveScheduler(anyLong(), schedulerCreateDto);

		// Then
		verify(memberService, times(1)).findById(anyLong());
		verify(schedulerRepository, times(1)).save(any());
		assertAll(
			() -> assertEquals("test", response.getResult().getTitle()),
			() -> assertEquals("test", response.getResult().getContent())
		);

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

	@Test
	@DisplayName("모든 일정 조회 성공")
	void success_getAllSchedulers() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		List<Scheduler> list = List.of(scheduler, scheduler);

		given(schedulerRepository.findAllByOrderByModifiedAtDesc(any()))
			.willReturn(new PageImpl<>(list, pageable, list.size()));

		// When
		CommonResponses<SchedulerDto> responses = schedulerService.getAllSchedulers(any());

		// Then
		List<SchedulerDto> result = responses.getResult();
		for (SchedulerDto schedulerDto : result) {
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

		given(scheduler.getTitle())
			.willReturn("test2");
		given(scheduler.getContent())
			.willReturn("test2");

		given(schedulerRepository.findById(anyLong()))
			.willReturn(Optional.of(scheduler));

		// When
		CommonResponse<SchedulerDto> response = schedulerService.updateScheduler(1L, 1L, schedulerUpdateDto);

		// Then
		assertAll(
			() -> assertEquals(response.getResult().getTitle(), schedulerUpdateDto.getTitle()),
			() -> assertEquals(response.getResult().getContent(), schedulerUpdateDto.getContent())
		);

	}

	@Test
	@DisplayName("일정 수정 실패 - 일정을 찾을 수 없음")
	void fail_updateScheduler_schedulerNotFound() {
		// Given
		given(schedulerRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// When
		SchedulerException exception = assertThrows(SchedulerException.class,
			() -> schedulerService.updateScheduler(1L, 1L, schedulerUpdateDto));

		// Then
		assertEquals(SchedulerExceptionCode.SCHEDULER_NOT_FOUND, exception.getErrorCode());

	}

	@Test
	@DisplayName("일정 수정 실패 - 유저 불일치")
	void fail_updateScheduler_userMismatch() {
		// Given
		given(schedulerRepository.findById(anyLong()))
			.willReturn(Optional.of(Scheduler.builder()
				.id(1L)
				.member(Member.builder()
					.id(1L)
					.email("test@test")
					.password("testtest")
					.build())
				.title("test")
				.content("test")
				.build()));

		// When
		SchedulerException exception = assertThrows(SchedulerException.class,
			() -> schedulerService.updateScheduler(2L, 1L, schedulerUpdateDto));

		// Then
		assertEquals(SchedulerExceptionCode.USER_MISMATCH, exception.getErrorCode());

	}

	@Test
	@DisplayName("일정 삭제 성공")
	void success_deleteScheduler() {
		// Given
		given(scheduler.getId())
			.willReturn(1L);

		given(schedulerRepository.findById(anyLong()))
			.willReturn(Optional.of(scheduler));

		// When

		CommonResponse<Long> response = schedulerService.deleteScheduler(1L, 1L);

		// Then
		assertAll(
			() -> assertEquals(scheduler.getId(), response.getResult())
		);

	}

	@Test
	@DisplayName("일정 삭제 실패 - 일정을 찾을 수 없음")
	void fail_deleteScheduler_schedulerNotFound() {
		// Given
		given(schedulerRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// When
		SchedulerException exception = assertThrows(SchedulerException.class,
			() -> schedulerService.deleteScheduler(1L, 1L));

		// Then
		assertEquals(SchedulerExceptionCode.SCHEDULER_NOT_FOUND, exception.getErrorCode());

	}

	@Test
	@DisplayName("일정 삭제 실패 - 유저 불일치")
	void fail_deleteScheduler_userMismatch() {
		// Given
		given(schedulerRepository.findById(anyLong()))
			.willReturn(Optional.of(Scheduler.builder()
				.id(1L)
				.member(Member.builder()
					.id(1L)
					.email("test@test")
					.password("testtest")
					.build())
				.title("test")
				.content("test")
				.build()));

		// When
		SchedulerException exception = assertThrows(SchedulerException.class,
			() -> schedulerService.deleteScheduler(2L, 1L));

		// Then
		assertEquals(SchedulerExceptionCode.USER_MISMATCH, exception.getErrorCode());

	}

}
