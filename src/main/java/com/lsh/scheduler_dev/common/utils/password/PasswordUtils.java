package com.lsh.scheduler_dev.common.utils.password;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.lsh.scheduler_dev.common.utils.password.exception.PasswordUtilsException;
import com.lsh.scheduler_dev.common.utils.password.exception.PasswordUtilsExceptionCode;

public class PasswordUtils {
	private static final String ALG = "HmacSHA3-256";
	private static final int ITERATIONS = 1000;
	private static final String SALT = "salt";

	public static String encryptPassword(String password) {
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
			throw new PasswordUtilsException(PasswordUtilsExceptionCode.ENCRYPT_ERROR);
		} catch (Exception e) {
			throw new PasswordUtilsException(PasswordUtilsExceptionCode.UNEXPECTED_ERROR);
		}
	}

	public static boolean matches(String rawPassword, String encodedPassword) {
		String hashedPassword = encryptPassword(rawPassword);
		return Objects.equals(hashedPassword, encodedPassword);
	}

}
