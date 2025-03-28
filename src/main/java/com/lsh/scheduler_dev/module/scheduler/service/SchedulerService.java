package com.lsh.scheduler_dev.module.scheduler.service;

import com.lsh.scheduler_dev.common.response.ListResponse;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.member.service.MemberService;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerCreateDto;
import com.lsh.scheduler_dev.module.scheduler.dto.request.SchedulerUpdateDto;
import com.lsh.scheduler_dev.module.scheduler.dto.response.SchedulerDto;
import com.lsh.scheduler_dev.module.scheduler.exception.SchedulerException;
import com.lsh.scheduler_dev.module.scheduler.exception.SchedulerExceptionCode;
import com.lsh.scheduler_dev.module.scheduler.repository.SchedulerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final SchedulerRepository schedulerRepository;
    private final MemberService memberService;

    public SchedulerDto saveScheduler(Long memberId, SchedulerCreateDto dto) {
        Member member = memberService.findById(memberId);

        Scheduler savedScheduler = schedulerRepository.save(Scheduler.builder()
                .member(member)
                .title(dto.getTitle())
                .content(dto.getContent())
                .build());

        return SchedulerDto.toDto(savedScheduler);
    }

    public ListResponse<SchedulerDto> findAllSchedulers(Pageable pageable) {
        return ListResponse.toListResponse(schedulerRepository.findAllByOrderByModifiedAtDesc(pageable)
                .map(SchedulerDto::toDto));
    }

    public SchedulerDto updateScheduler(Long memberId, Long schedulerId, SchedulerUpdateDto schedulerUpdateDto) {
        Scheduler scheduler = findById(schedulerId);

        Member member = memberService.findById(memberId);

        validate(scheduler, member);

        scheduler.updateTitle(schedulerUpdateDto.getTitle());
        scheduler.updateContent(schedulerUpdateDto.getContent());
        Scheduler updatedScheduler = schedulerRepository.save(scheduler);

        return SchedulerDto.toDto(updatedScheduler);
    }

    public SchedulerDto deleteScheduler(Long memberId, Long schedulerId) {
        Scheduler scheduler = findById(schedulerId);


        Member member = memberService.findById(memberId);

        validate(scheduler, member);

        schedulerRepository.delete(scheduler);

        return SchedulerDto.toDto(scheduler);
    }

    public Scheduler findById(Long schedulerId) {
        return schedulerRepository.findById(schedulerId)
                .orElseThrow(() -> new SchedulerException(SchedulerExceptionCode.SCHEDULER_NOT_FOUND));
    }

    public void plusCommentCount(Scheduler scheduler) {
        scheduler.plusCommentCount();

        schedulerRepository.save(scheduler);
    }

    public void minusCommentCount(Scheduler scheduler) {
        scheduler.minusCommentCount();

        schedulerRepository.save(scheduler);
    }

    private void validate(Scheduler scheduler, Member member) {
        if (!Objects.equals(scheduler.getMember().getId(), member.getId())) {
            throw new SchedulerException(SchedulerExceptionCode.USER_MISMATCH);
        }
    }
}
