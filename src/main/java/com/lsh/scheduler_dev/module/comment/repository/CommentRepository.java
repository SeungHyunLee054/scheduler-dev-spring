package com.lsh.scheduler_dev.module.comment.repository;

import com.lsh.scheduler_dev.module.comment.domain.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllBySchedulerIdOrderByModifiedAtDesc(Long schedulerId, Pageable pageable);
}
