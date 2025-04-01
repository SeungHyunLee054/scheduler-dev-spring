package com.lsh.scheduler_dev.module.member.domain.model;

import com.lsh.scheduler_dev.common.jpa.audit.BaseEntity;
import com.lsh.scheduler_dev.common.utils.password.PasswordUtils;
import com.lsh.scheduler_dev.module.comment.domain.model.Comment;
import com.lsh.scheduler_dev.module.member.exception.MemberException;
import com.lsh.scheduler_dev.module.member.exception.MemberExceptionCode;
import com.lsh.scheduler_dev.module.scheduler.domain.model.Scheduler;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    public void checkPassword(String password) {
        if (!PasswordUtils.matches(password, this.password)) {
            throw new MemberException(MemberExceptionCode.FAIL_SIGN_IN);
        }
    }

}
