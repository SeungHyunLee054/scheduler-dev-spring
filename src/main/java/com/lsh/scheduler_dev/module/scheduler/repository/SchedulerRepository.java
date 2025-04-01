package com.lsh.scheduler_dev.module.scheduler.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;

public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {
	Page<Scheduler> findAllByOrderByModifiedAtDesc(Pageable pageable);
}
