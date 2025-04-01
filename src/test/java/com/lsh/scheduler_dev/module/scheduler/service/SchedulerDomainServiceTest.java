package com.lsh.scheduler_dev.module.scheduler.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerCreateDto;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerUpdateDto;
import com.lsh.scheduler_dev.module.scheduler.exception.SchedulerException;
import com.lsh.scheduler_dev.module.scheduler.exception.SchedulerExceptionCode;
import com.lsh.scheduler_dev.module.scheduler.repository.SchedulerRepository;

@ExtendWith(MockitoExtension.class)
class SchedulerDomainServiceTest {
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
	private SchedulerDomainService schedulerDomainService;

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

		given(schedulerRepository.save(any()))
			.willReturn(scheduler);

		// When
		Scheduler savedScheduler = schedulerDomainService.saveScheduler(member, schedulerCreateDto);

		// Then
		verify(schedulerRepository, times(1)).save(any());
		assertAll(
			() -> assertEquals("test", savedScheduler.getTitle()),
			() -> assertEquals("test", savedScheduler.getContent())
		);

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
		Page<Scheduler> result = schedulerDomainService.getAllSchedulers(any());

		// Then
		List<Scheduler> content = result.getContent();
		for (Scheduler s : content) {
			assertAll(
				() -> assertEquals(s.getId(), scheduler.getId()),
				() -> assertEquals(s.getTitle(), scheduler.getTitle()),
				() -> assertEquals(s.getContent(), scheduler.getContent())
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
		Scheduler updatedScheduler =
			schedulerDomainService.updateScheduler(1L, 1L, schedulerUpdateDto);

		// Then
		assertAll(
			() -> assertEquals(updatedScheduler.getTitle(), schedulerUpdateDto.getTitle()),
			() -> assertEquals(updatedScheduler.getContent(), schedulerUpdateDto.getContent())
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
			() -> schedulerDomainService.updateScheduler(1L, 1L, schedulerUpdateDto));

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
			() -> schedulerDomainService.updateScheduler(2L, 1L, schedulerUpdateDto));

		// Then
		assertEquals(SchedulerExceptionCode.USER_MISMATCH, exception.getErrorCode());

	}

	@Test
	@DisplayName("일정 삭제 성공")
	void success_deleteScheduler() {
		// Given
		given(scheduler.getId())
			.willReturn(1L);
		given(scheduler.getMember())
			.willReturn(member);

		given(schedulerRepository.findById(anyLong()))
			.willReturn(Optional.of(scheduler));

		// When

		Scheduler deletedScheduler = schedulerDomainService.deleteScheduler(1L, 1L);

		// Then
		assertAll(
			() -> assertEquals(scheduler.getId(), deletedScheduler.getId()),
			() -> assertEquals(scheduler.getMember().getName(), deletedScheduler.getMember().getName()),
			() -> assertEquals(scheduler.getTitle(), deletedScheduler.getTitle()),
			() -> assertEquals(scheduler.getContent(), deletedScheduler.getContent())
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
			() -> schedulerDomainService.deleteScheduler(1L, 1L));

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
			() -> schedulerDomainService.deleteScheduler(2L, 1L));

		// Then
		assertEquals(SchedulerExceptionCode.USER_MISMATCH, exception.getErrorCode());

	}

	@Test
	@DisplayName("댓글 수 증가 성공")
	void success_plusCommentCount() {
		// Given
		Scheduler s = Scheduler.builder()
			.title("test")
			.content("test")
			.commentCount(0)
			.build();

		// When
		schedulerDomainService.plusCommentCount(s);

		// Then
		assertEquals(1, s.getCommentCount());

	}

	@Test
	@DisplayName("댓글 수 감소 성공")
	void success_minusCommentCount() {
		// Given
		Scheduler s = Scheduler.builder()
			.title("test")
			.content("test")
			.commentCount(0)
			.build();

		// When
		schedulerDomainService.minusCommentCount(s);

		// Then
		assertEquals(0, s.getCommentCount());

	}
}