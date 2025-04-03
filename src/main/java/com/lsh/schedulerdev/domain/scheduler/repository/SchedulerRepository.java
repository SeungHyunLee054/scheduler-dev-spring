package com.lsh.schedulerdev.domain.scheduler.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lsh.schedulerdev.domain.scheduler.entity.Scheduler;

public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {
	Page<Scheduler> findAllByOrderByModifiedAtDesc(Pageable pageable);
}
