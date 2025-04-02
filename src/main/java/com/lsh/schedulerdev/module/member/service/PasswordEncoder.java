package com.lsh.schedulerdev.module.member.service;

public interface PasswordEncoder {
	String encode(String password);

	boolean matches(String rawPassword, String encodedPassword);
}
