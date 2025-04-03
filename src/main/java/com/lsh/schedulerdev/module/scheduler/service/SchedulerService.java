package com.lsh.schedulerdev.module.scheduler.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lsh.schedulerdev.common.response.CommonResponse;
import com.lsh.schedulerdev.common.response.CommonResponses;
import com.lsh.schedulerdev.module.member.domain.model.Member;
import com.lsh.schedulerdev.module.member.service.MemberService;
import com.lsh.schedulerdev.module.scheduler.domain.model.Scheduler;
import com.lsh.schedulerdev.module.scheduler.dto.request.SchedulerCreateDto;
import com.lsh.schedulerdev.module.scheduler.dto.request.SchedulerUpdateDto;
import com.lsh.schedulerdev.module.scheduler.dto.response.SchedulerDto;
import com.lsh.schedulerdev.module.scheduler.exception.SchedulerException;
import com.lsh.schedulerdev.module.scheduler.exception.SchedulerExceptionCode;
import com.lsh.schedulerdev.module.scheduler.repository.SchedulerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedulerService {
	private final SchedulerRepository schedulerRepository;
	private final MemberService memberService;

	/**
	 * 일정 저장
	 *
	 * @param memberId           유저 id
	 * @param schedulerCreateDto 작성하려는 일정 내용
	 * @return 일정 정보
	 */
	@Transactional
	public CommonResponse<SchedulerDto> saveScheduler(Long memberId, SchedulerCreateDto schedulerCreateDto) {
		Member member = memberService.findById(memberId);

		Scheduler scheduler = schedulerRepository.save(Scheduler.builder()
			.member(member)
			.title(schedulerCreateDto.getTitle())
			.content(schedulerCreateDto.getContent())
			.build());

		return CommonResponse.of("일정 생성 성공", SchedulerDto.from(scheduler));
	}

	/**
	 * 모든 일정 조회
	 *
	 * @param pageable 페이지 값
	 * @return Page에서 원하는 정보 값만 담은 List를 반환
	 */
	public CommonResponses<SchedulerDto> getAllSchedulers(Pageable pageable) {
		Page<SchedulerDto> schedulerDtoPage =
			schedulerRepository.findAllByOrderByModifiedAtDesc(pageable)
				.map(SchedulerDto::from);

		return CommonResponses.from("모든 일정 조회 성공", schedulerDtoPage);
	}

	/**
	 * 일정 수정
	 *
	 * @param memberId           일정을 작성한 유저 id
	 * @param schedulerId        일정 id
	 * @param schedulerUpdateDto 수정할 내용
	 * @return 일정 정보
	 */
	@Transactional
	public CommonResponse<SchedulerDto> updateScheduler(Long memberId, Long schedulerId,
		SchedulerUpdateDto schedulerUpdateDto) {
		Scheduler scheduler = findById(schedulerId);

		scheduler.validateMember(memberId);

		scheduler.updateScheduler(schedulerUpdateDto.getTitle(), schedulerUpdateDto.getContent());

		return CommonResponse.of("일정 수정 성공", SchedulerDto.from(scheduler));
	}

	/**
	 * 일정 삭제
	 *
	 * @param memberId    일정을 작성한 유저 id
	 * @param schedulerId 일정 id
	 * @return 삭제된 일정 정보
	 */
	@Transactional
	public CommonResponse<Long> deleteScheduler(Long memberId, Long schedulerId) {
		Scheduler scheduler = findById(schedulerId);

		scheduler.validateMember(memberId);

		schedulerRepository.delete(scheduler);

		return CommonResponse.of("일정 삭제 성공", scheduler.getId());
	}

	/**
	 * 일정 조회
	 *
	 * @param schedulerId 일정 id
	 * @return id 값으로 조회된 일정
	 */
	public Scheduler findById(Long schedulerId) {
		return schedulerRepository.findById(schedulerId)
			.orElseThrow(() -> new SchedulerException(SchedulerExceptionCode.SCHEDULER_NOT_FOUND));
	}

}
