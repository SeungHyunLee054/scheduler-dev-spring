package com.lsh.scheduler_dev.module.scheduler.service;

import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.member.repository.MemberRepository;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import com.lsh.scheduler_dev.module.scheduler.dto.SchedulerCreateRequestDto;
import com.lsh.scheduler_dev.module.scheduler.dto.SchedulerResponseDto;
import com.lsh.scheduler_dev.module.scheduler.dto.SchedulerUpdateRequestDto;
import com.lsh.scheduler_dev.module.scheduler.repository.SchedulerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final SchedulerRepository schedulerRepository;
    private final MemberRepository memberRepository;

    public SchedulerResponseDto saveScheduler(Long memberId, SchedulerCreateRequestDto dto) {
        Member member = memberRepository.findById(memberId).orElse(null);

        Scheduler savedScheduler = schedulerRepository.save(Scheduler.builder()
                .member(member)
                .title(dto.getTitle())
                .content(dto.getContent())
                .build());

        return SchedulerResponseDto.toDto(savedScheduler);
    }

    public List<SchedulerResponseDto> findAllSchedulers() {
        return schedulerRepository.findAll().stream().map(SchedulerResponseDto::toDto).collect(Collectors.toList());
    }

    public SchedulerResponseDto updateScheduler(Long memberId, Long schedulerId, SchedulerUpdateRequestDto dto) {
        Scheduler scheduler = schedulerRepository.findById(schedulerId).orElse(null);
        Member member = memberRepository.findById(memberId).orElse(null);
        if (!scheduler.getMember().getId().equals(member.getId())) {
            //exception
        }

        scheduler.updateTitle(dto.getTitle());
        scheduler.updateContent(dto.getContent());
        Scheduler updatedScheduler = schedulerRepository.save(scheduler);

        return SchedulerResponseDto.toDto(updatedScheduler);
    }

    public SchedulerResponseDto deleteScheduler(Long memberId, Long schedulerId) {
        Scheduler scheduler = schedulerRepository.findById(schedulerId).orElse(null);
        Member member = memberRepository.findById(memberId).orElse(null);
        if (!scheduler.getMember().getId().equals(member.getId())) {
            //exception
        }

        schedulerRepository.delete(scheduler);

        return SchedulerResponseDto.toDto(scheduler);
    }
}
