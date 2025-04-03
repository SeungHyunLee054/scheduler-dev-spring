package com.lsh.schedulerdev.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberUpdateDto {
	@NotNull(message = "이름은 필수 입력 값입니다.")
	@NotBlank(message = "이름은 공백이 아니어야 합니다.")
	@Size(max = 4, message = "이름은 4글자 내여야 합니다.")
	private String name;

	@NotNull(message = "비밀번호는 필수 입력 값입니다.")
	@NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
	@Size(min = 8, message = "비밀번호는 8글자 이상이어야 합니다.")
	private String password;
}
