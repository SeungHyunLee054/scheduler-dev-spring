package com.lsh.schedulerdev.module.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lsh.schedulerdev.module.comment.domain.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Page<Comment> findAllBySchedulerIdOrderByModifiedAtDesc(Long schedulerId, Pageable pageable);
}
