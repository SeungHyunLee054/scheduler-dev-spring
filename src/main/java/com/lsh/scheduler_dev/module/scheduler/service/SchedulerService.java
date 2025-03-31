package com.lsh.scheduler_dev.module.scheduler.service;

import com.lsh.scheduler_dev.common.response.ListResponse;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerCreateDto;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerUpdateDto;
import com.lsh.scheduler_dev.module.scheduler.dto.response.SchedulerDto;
import com.lsh.scheduler_dev.module.scheduler.exception.SchedulerException;
import com.lsh.scheduler_dev.module.scheduler.exception.SchedulerExceptionCode;
import com.lsh.scheduler_dev.module.scheduler.repository.SchedulerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final SchedulerRepository schedulerRepository;

    public SchedulerDto saveScheduler(Member member, SchedulerCreateDto dto) {
        Scheduler savedScheduler = schedulerRepository.save(Scheduler.builder()
                .member(member)
                .title(dto.getTitle())
                .content(dto.getContent())
                .build());

        return SchedulerDto.toDto(savedScheduler);
    }

    public ListResponse<SchedulerDto> getAllSchedulers(Pageable pageable) {
        return ListResponse.toListResponse(schedulerRepository.findAllByOrderByModifiedAtDesc(pageable)
                .map(SchedulerDto::toDto));
    }

    @Transactional
    public SchedulerDto updateScheduler(Long memberId, Long schedulerId, SchedulerUpdateDto schedulerUpdateDto) {
        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .filter(s -> s.getMember().getId().equals(memberId))
                .orElseThrow(() -> new SchedulerException(SchedulerExceptionCode.USER_MISMATCH));

        scheduler.updateScheduler(schedulerUpdateDto.getTitle(), schedulerUpdateDto.getContent());

        return SchedulerDto.toDto(scheduler);
    }

    @Transactional
    public SchedulerDto deleteScheduler(Long memberId, Long schedulerId) {
        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .filter(s -> s.getMember().getId().equals(memberId))
                .orElseThrow(() -> new SchedulerException(SchedulerExceptionCode.USER_MISMATCH));

        schedulerRepository.delete(scheduler);

        return SchedulerDto.toDto(scheduler);
    }

    public Scheduler findById(Long schedulerId) {
        return schedulerRepository.findById(schedulerId)
                .orElseThrow(() -> new SchedulerException(SchedulerExceptionCode.SCHEDULER_NOT_FOUND));
    }

    @Transactional
    public void plusCommentCount(Scheduler scheduler) {
        scheduler.plusCommentCount();
    }

    @Transactional
    public void minusCommentCount(Scheduler scheduler) {
        scheduler.minusCommentCount();
    }

}
