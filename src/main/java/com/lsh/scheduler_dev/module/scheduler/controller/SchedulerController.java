package com.lsh.scheduler_dev.module.scheduler.controller;

import com.lsh.scheduler_dev.module.member.dto.MemberAuthDto;
import com.lsh.scheduler_dev.module.scheduler.dto.SchedulerCreateRequestDto;
import com.lsh.scheduler_dev.module.scheduler.dto.SchedulerResponseDto;
import com.lsh.scheduler_dev.module.scheduler.dto.SchedulerUpdateRequestDto;
import com.lsh.scheduler_dev.module.scheduler.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/schedulers")
@RequiredArgsConstructor
public class SchedulerController {
    private final SchedulerService schedulerService;

    @PostMapping
    public ResponseEntity<SchedulerResponseDto> create(
            @SessionAttribute(name = "Authorization") MemberAuthDto memberAuthDto,
            @RequestBody SchedulerCreateRequestDto dto
    ) {
        return ResponseEntity.ok(schedulerService.saveScheduler(memberAuthDto.getMemberId(), dto));
    }

    @GetMapping
    public ResponseEntity<List<SchedulerResponseDto>> getAllSchedulers() {
        return ResponseEntity.ok(schedulerService.findAllSchedulers());
    }

    @PutMapping("/{schedulerId}")
    public ResponseEntity<SchedulerResponseDto> updateScheduler(
            @SessionAttribute(name = "Authorization") MemberAuthDto memberAuthDto,
            @PathVariable Long schedulerId,
            @RequestBody SchedulerUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(schedulerService.updateScheduler(memberAuthDto.getMemberId(), schedulerId, dto));
    }

    @DeleteMapping("{schedulerId}")
    public ResponseEntity<SchedulerResponseDto> deleteScheduler(
            @SessionAttribute(name = "Authorization") MemberAuthDto memberAuthDto,
            @PathVariable Long schedulerId
    ) {
        return ResponseEntity.ok(schedulerService.deleteScheduler(memberAuthDto.getMemberId(), schedulerId));
    }
}
