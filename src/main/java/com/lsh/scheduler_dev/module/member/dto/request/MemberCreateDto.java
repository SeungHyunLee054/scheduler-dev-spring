package com.lsh.scheduler_dev.module.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MemberCreateDto {
    @NotNull(message = "이름은 필수 입력 값입니다.")
    @NotBlank(message = "이름은 공백이 아니어야 합니다.")
    @Size(max = 4, message = "이름은 4글자 내여야 합니다.")
    private String name;

    @NotNull(message = "이메일은 필수 입력 값입니다.")
    @NotBlank(message = "이메일은 공백이 아니어야 합니다.")
    @Email(message = "이메일 형식이 잘못되었습니다.")
    private String email;

    @NotNull(message = "비밀번호는 필수 입력 값입니다.")
    @NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
    @Size(max = 8, message = "비밀번호는 8글자 이상이어야 합니다.")
    private String password;
}
