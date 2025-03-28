package com.lsh.scheduler_dev.common.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;


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
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        String hashedPassword = encryptPassword(rawPassword);
        return Objects.equals(hashedPassword, encodedPassword);
    }

}
