package com.lsh.scheduler_dev.common.security.jwt.provieder.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenExpiredConstant {
    private static final long MILLISECOND = 1000L;

    @Value("${spring.jwt.token.expired.second}")
    private long seconds;

    @Value("${spring.jwt.token.expired.minute}")
    private long minutes;

    @Value("${spring.jwt.token.expired.hour}")
    private long hours;

    public long getAccessTokenExpiredTime() {
        return hours * minutes * seconds * MILLISECOND;
    }

    public Date getAccessTokenExpiredDate(Date date) {
        return new Date(date.getTime() + getAccessTokenExpiredTime());
    }
}
