package com.lsh.scheduler_dev.module.scheduler.controller;

import com.lsh.scheduler_dev.common.constants.SessionConstants;
import com.lsh.scheduler_dev.common.response.ListResponse;
import com.lsh.scheduler_dev.module.member.dto.MemberAuthDto;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerCreateDto;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerUpdateDto;
import com.lsh.scheduler_dev.module.scheduler.dto.response.SchedulerDto;
import com.lsh.scheduler_dev.module.scheduler.facade.SchedulerFacade;
import com.lsh.scheduler_dev.module.scheduler.service.SchedulerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/schedulers")
@RequiredArgsConstructor
public class SchedulerController {
    private final SchedulerService schedulerService;
    private final SchedulerFacade schedulerFacade;

    @PostMapping
    public ResponseEntity<SchedulerDto> createScheduler(
            @SessionAttribute(name = SessionConstants.AUTHORIZATION, required = false) MemberAuthDto memberAuthDto,
            @Valid @RequestBody SchedulerCreateDto schedulerCreateDto
    ) {
        return ResponseEntity.ok(schedulerFacade.saveScheduler(memberAuthDto.getMemberId(), schedulerCreateDto));
    }

    @GetMapping
    public ResponseEntity<ListResponse<SchedulerDto>> getAllSchedulers(
            @RequestParam(defaultValue = "0") Integer pageIdx,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok(schedulerService.getAllSchedulers(PageRequest.of(pageIdx, pageSize)));
    }

    @PutMapping("/{schedulerId}")
    public ResponseEntity<SchedulerDto> updateScheduler(
            @SessionAttribute(name = SessionConstants.AUTHORIZATION, required = false) MemberAuthDto memberAuthDto,
            @PathVariable Long schedulerId,
            @Valid @RequestBody SchedulerUpdateDto schedulerUpdateDto
    ) {
        return ResponseEntity.ok(schedulerService.updateScheduler(memberAuthDto.getMemberId(), schedulerId, schedulerUpdateDto));
    }

    @DeleteMapping("{schedulerId}")
    public ResponseEntity<SchedulerDto> deleteScheduler(
            @SessionAttribute(name = SessionConstants.AUTHORIZATION, required = false) MemberAuthDto memberAuthDto,
            @PathVariable Long schedulerId
    ) {
        return ResponseEntity.ok(schedulerService.deleteScheduler(memberAuthDto.getMemberId(), schedulerId));
    }
}
