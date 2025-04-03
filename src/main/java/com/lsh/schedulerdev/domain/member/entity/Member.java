package com.lsh.schedulerdev.domain.member.entity;

import java.util.List;

import com.lsh.schedulerdev.common.audit.BaseEntity;
import com.lsh.schedulerdev.domain.comment.entity.Comment;
import com.lsh.schedulerdev.domain.member.exception.MemberException;
import com.lsh.schedulerdev.domain.member.exception.MemberExceptionCode;
import com.lsh.schedulerdev.domain.member.service.PasswordEncoder;
import com.lsh.schedulerdev.domain.scheduler.entity.Scheduler;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Member extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	@NonNull
	private String email;

	@Column(nullable = false)
	@NonNull
	private String password;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<Scheduler> schedulers;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<Comment> comments;

	public void updateMember(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public void checkPassword(PasswordEncoder passwordEncoder, String password) {
		if (!passwordEncoder.matches(password, this.password)) {
			throw new MemberException(MemberExceptionCode.FAIL_SIGN_IN);
		}
	}

}
