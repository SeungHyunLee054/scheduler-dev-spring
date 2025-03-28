package com.lsh.scheduler_dev.common.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SessionExpiredConstant {
    @Value("${spring.session.expired.second}")
    private int seconds;

    @Value("${spring.session.expired.minute}")
    private int minutes;

    @Value("${spring.session.expired.hour}")
    private int hours;

    private SessionExpiredConstant() {
    }

    public int getSessionExpiredTime() {
        return seconds * minutes * hours;
    }
}
