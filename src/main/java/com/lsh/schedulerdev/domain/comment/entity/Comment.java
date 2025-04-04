package com.lsh.schedulerdev.domain.comment.entity;

import com.lsh.schedulerdev.common.audit.BaseEntity;
import com.lsh.schedulerdev.domain.comment.code.CommentExceptionCode;
import com.lsh.schedulerdev.domain.comment.exception.CommentException;
import com.lsh.schedulerdev.domain.member.entity.Member;
import com.lsh.schedulerdev.domain.scheduler.entity.Scheduler;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@NonNull
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scheduler_id")
	private Scheduler scheduler;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	public void updateContent(String content) {
		this.content = content;
	}

	public void validateMember(Long memberId) {
		if (!memberId.equals(this.member.getId())) {
			throw new CommentException(CommentExceptionCode.USER_MISMATCH);
		}
	}

	public Long getMemberId() {
		return this.member.getId();
	}

	public Long getSchedulerId() {
		return this.scheduler.getId();
	}
}
