package com.lsh.schedulerdev.module.scheduler.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.lsh.schedulerdev.common.constants.SessionConstants;
import com.lsh.schedulerdev.common.response.CommonResponse;
import com.lsh.schedulerdev.common.response.CommonResponses;
import com.lsh.schedulerdev.module.member.dto.MemberAuthDto;
import com.lsh.schedulerdev.module.scheduler.application.SchedulerService;
import com.lsh.schedulerdev.module.scheduler.dto.request.SchedulerCreateDto;
import com.lsh.schedulerdev.module.scheduler.dto.request.SchedulerUpdateDto;
import com.lsh.schedulerdev.module.scheduler.dto.response.SchedulerDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedulers")
@RequiredArgsConstructor
public class SchedulerController {
	private final SchedulerService schedulerService;

	@PostMapping
	public ResponseEntity<CommonResponse<SchedulerDto>> createScheduler(
		@SessionAttribute(name = SessionConstants.AUTHORIZATION) MemberAuthDto memberAuthDto,
		@Valid @RequestBody SchedulerCreateDto schedulerCreateDto
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(schedulerService.saveScheduler(memberAuthDto.getMemberId(), schedulerCreateDto));
	}

	@GetMapping
	public ResponseEntity<CommonResponses<SchedulerDto>> getAllSchedulers(
		@RequestParam(defaultValue = "0") Integer pageIdx,
		@RequestParam(defaultValue = "10") Integer pageSize
	) {
		return ResponseEntity.ok(schedulerService.getAllSchedulers(PageRequest.of(pageIdx, pageSize)));
	}

	@PutMapping("/{schedulerId}")
	public ResponseEntity<CommonResponse<SchedulerDto>> updateScheduler(
		@SessionAttribute(name = SessionConstants.AUTHORIZATION) MemberAuthDto memberAuthDto,
		@PathVariable Long schedulerId,
		@Valid @RequestBody SchedulerUpdateDto schedulerUpdateDto
	) {
		return ResponseEntity.ok(
			schedulerService.updateScheduler(memberAuthDto.getMemberId(), schedulerId, schedulerUpdateDto));
	}

	@DeleteMapping("{schedulerId}")
	public ResponseEntity<CommonResponse<Long>> deleteScheduler(
		@SessionAttribute(name = SessionConstants.AUTHORIZATION) MemberAuthDto memberAuthDto,
		@PathVariable Long schedulerId
	) {
		return ResponseEntity.ok(schedulerService.deleteScheduler(memberAuthDto.getMemberId(), schedulerId));
	}
}
