package com.lsh.scheduler_dev.module.scheduler.facade;

import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.member.service.MemberService;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerCreateDto;
import com.lsh.scheduler_dev.module.scheduler.dto.response.SchedulerDto;
import com.lsh.scheduler_dev.module.scheduler.service.SchedulerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerFacade {
    private final SchedulerService schedulerService;
    private final MemberService memberService;

    @Transactional
    public SchedulerDto saveScheduler(Long memberId, SchedulerCreateDto schedulerCreateDto) {
        Member member = memberService.findById(memberId);

        return schedulerService.saveScheduler(member, schedulerCreateDto);
    }

}
