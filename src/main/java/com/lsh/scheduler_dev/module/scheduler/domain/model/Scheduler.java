package com.lsh.scheduler_dev.module.scheduler.domain.model;

import com.lsh.scheduler_dev.common.jpa.audit.BaseEntity;
import com.lsh.scheduler_dev.module.comment.domain.model.Comment;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String title;

    @Column(nullable = false)
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

}
