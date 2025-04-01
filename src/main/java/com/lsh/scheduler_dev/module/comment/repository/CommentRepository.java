package com.lsh.scheduler_dev.module.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lsh.scheduler_dev.module.comment.domain.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Page<Comment> findAllBySchedulerIdOrderByModifiedAtDesc(Long schedulerId, Pageable pageable);
}
