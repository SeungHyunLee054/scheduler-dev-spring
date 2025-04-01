package com.lsh.schedulerdev.module.scheduler.domain.model;

import java.util.List;

import com.lsh.schedulerdev.common.audit.BaseEntity;
import com.lsh.schedulerdev.module.comment.domain.model.Comment;
import com.lsh.schedulerdev.module.member.domain.model.Member;
import com.lsh.schedulerdev.module.scheduler.exception.SchedulerException;
import com.lsh.schedulerdev.module.scheduler.exception.SchedulerExceptionCode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Scheduler extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(nullable = false)
	@NonNull
	private String title;

	@Column(nullable = false)
	@NonNull
	private String content;

	private int commentCount;

	@OneToMany(mappedBy = "scheduler", cascade = CascadeType.ALL)
	private List<Comment> comments;

	public void updateScheduler(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void plusCommentCount() {
		++this.commentCount;
	}

	public void minusCommentCount() {
		this.commentCount = Math.max(0, --this.commentCount);
	}

	public void validateMember(Long memberId) {
		if (!this.member.getId().equals(memberId)) {
			throw new SchedulerException(SchedulerExceptionCode.USER_MISMATCH);
		}
	}

}
