package com.lsh.schedulerdev.module.member.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lsh.schedulerdev.module.member.domain.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	Page<Member> findAllByOrderByModifiedAtDesc(Pageable pageable);

	boolean existsByEmail(String email);
}
