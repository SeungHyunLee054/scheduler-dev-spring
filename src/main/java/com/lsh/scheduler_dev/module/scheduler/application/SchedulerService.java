package com.lsh.scheduler_dev.module.scheduler.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lsh.scheduler_dev.common.response.CommonResponse;
import com.lsh.scheduler_dev.common.response.CommonResponses;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.member.service.MemberService;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerCreateDto;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerUpdateDto;
import com.lsh.scheduler_dev.module.scheduler.dto.response.SchedulerDto;
import com.lsh.scheduler_dev.module.scheduler.service.SchedulerDomainService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedulerService {
	private final SchedulerDomainService schedulerDomainService;
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

		Scheduler scheduler = schedulerDomainService.saveScheduler(member, schedulerCreateDto);

		SchedulerDto schedulerDto = SchedulerDto.from(scheduler);

		return CommonResponse.of("일정 생성 성공", schedulerDto);
	}

	/**
	 * 모든 일정 조회
	 *
	 * @param pageable 페이지 값
	 * @return Page에서 원하는 정보 값만 담은 List를 반환
	 */
	public CommonResponses<SchedulerDto> getAllSchedulers(Pageable pageable) {
		Page<Scheduler> schedulerPage = schedulerDomainService.getAllSchedulers(pageable);
		Page<SchedulerDto> schedulerDtoPage = schedulerPage.map(SchedulerDto::from);

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
		Scheduler updatedScheduler =
			schedulerDomainService.updateScheduler(memberId, schedulerId, schedulerUpdateDto);

		SchedulerDto schedulerDto = SchedulerDto.from(updatedScheduler);

		return CommonResponse.of("일정 수정 성공", schedulerDto);
	}

	/**
	 * 일정 삭제
	 *
	 * @param memberId    일정을 작성한 유저 id
	 * @param schedulerId 일정 id
	 * @return 삭제된 일정 정보
	 */
	@Transactional
	public CommonResponse<SchedulerDto> deleteScheduler(Long memberId, Long schedulerId) {
		Scheduler deletedScheduler = schedulerDomainService.deleteScheduler(memberId, schedulerId);

		SchedulerDto schedulerDto = SchedulerDto.from(deletedScheduler);

		return CommonResponse.of("일정 삭제 성공", schedulerDto);
	}

}
