package com.lsh.scheduler_dev.module.member.domain.model;

import com.lsh.scheduler_dev.common.jpa.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public void updateName(String newName) {
        this.name = newName;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

}
