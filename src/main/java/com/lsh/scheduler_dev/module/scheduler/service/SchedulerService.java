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

    /**
     * 일정 저장
     *
     * @param member             일정을 작성한 유저
     * @param schedulerCreateDto 작성하려는 일정 내용
     * @return 일정 정보
     */
    public SchedulerDto saveScheduler(Member member, SchedulerCreateDto schedulerCreateDto) {
        Scheduler savedScheduler = schedulerRepository.save(Scheduler.builder()
                .member(member)
                .title(schedulerCreateDto.getTitle())
                .content(schedulerCreateDto.getContent())
                .build());

        return SchedulerDto.from(savedScheduler);
    }

    /**
     * 모든 일정 조회
     *
     * @param pageable 페이지 값
     * @return Page에서 원하는 정보 값만 담은 List를 반환
     */
    public ListResponse<SchedulerDto> getAllSchedulers(Pageable pageable) {
        return ListResponse.toListResponse(schedulerRepository.findAllByOrderByModifiedAtDesc(pageable)
                .map(SchedulerDto::from));
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
    public SchedulerDto updateScheduler(Long memberId, Long schedulerId, SchedulerUpdateDto schedulerUpdateDto) {
        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .filter(s -> s.getMember().getId().equals(memberId))
                .orElseThrow(() -> new SchedulerException(SchedulerExceptionCode.USER_MISMATCH));

        scheduler.updateScheduler(schedulerUpdateDto.getTitle(), schedulerUpdateDto.getContent());

        return SchedulerDto.from(scheduler);
    }

    /**
     * 일정 삭제
     *
     * @param memberId    일정을 작성한 유저 id
     * @param schedulerId 일정 id
     * @return 삭제된 일정 정보
     */
    @Transactional
    public SchedulerDto deleteScheduler(Long memberId, Long schedulerId) {
        Scheduler scheduler = schedulerRepository.findById(schedulerId)
                .filter(s -> s.getMember().getId().equals(memberId))
                .orElseThrow(() -> new SchedulerException(SchedulerExceptionCode.USER_MISMATCH));

        schedulerRepository.delete(scheduler);

        return SchedulerDto.from(scheduler);
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

    /**
     * 댓글 수 증가
     *
     * @param scheduler 일정
     */
    @Transactional
    public void plusCommentCount(Scheduler scheduler) {
        scheduler.plusCommentCount();
    }

    /**
     * 댓글 수 감소
     *
     * @param scheduler 일정
     */
    @Transactional
    public void minusCommentCount(Scheduler scheduler) {
        scheduler.minusCommentCount();
    }

}
