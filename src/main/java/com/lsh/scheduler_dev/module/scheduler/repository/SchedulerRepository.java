package com.lsh.scheduler_dev.module.scheduler.repository;

import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {
}
