package com.lsh.schedulerdev.domain.member.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.lsh.schedulerdev.domain.member.code.PasswordEncoderExceptionCode;
import com.lsh.schedulerdev.domain.member.exception.PasswordEncoderException;

public class Sha3PasswordEncoder implements PasswordEncoder {
	private static final String ALG = "HmacSHA3-256";
	private static final int ITERATIONS = 1000;
	private static final String SALT = "salt";

	@Override
	public String encode(String password) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(SALT.getBytes(StandardCharsets.UTF_8), ALG);
			Mac mac = Mac.getInstance(ALG);
			mac.init(keySpec);

			byte[] hashedPassword = mac.doFinal(password.getBytes(StandardCharsets.UTF_8));
			for (int i = 1; i < ITERATIONS; i++) {
				hashedPassword = mac.doFinal(hashedPassword);
			}

			return Base64.getEncoder().encodeToString(hashedPassword);
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			throw new PasswordEncoderException(PasswordEncoderExceptionCode.ENCRYPT_ERROR);
		} catch (Exception e) {
			throw new PasswordEncoderException(PasswordEncoderExceptionCode.UNEXPECTED_ERROR);
		}
	}

	@Override
	public boolean matches(String rawPassword, String encodedPassword) {
		String hashedPassword = encode(rawPassword);
		return Objects.equals(hashedPassword, encodedPassword);
	}

}
