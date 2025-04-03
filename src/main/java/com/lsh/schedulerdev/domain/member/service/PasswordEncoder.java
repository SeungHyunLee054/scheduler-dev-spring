package com.lsh.schedulerdev.domain.member.service;

public interface PasswordEncoder {
	String encode(String password);

	boolean matches(String rawPassword, String encodedPassword);
}
