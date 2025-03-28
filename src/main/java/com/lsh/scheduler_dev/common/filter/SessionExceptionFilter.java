package com.lsh.scheduler_dev.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsh.scheduler_dev.common.exception.dto.ErrorResponse;
import com.lsh.scheduler_dev.common.filter.exception.FilterException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class SessionExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            doFilter(request, response, filterChain);
        } catch (FilterException e) {
            log.error(e.getMessage(), e);
            log.error(Arrays.toString(e.getStackTrace()));
            response.setStatus(e.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(),
                    ErrorResponse.builder()
                            .errorCode(e.getErrorCode().name())
                            .errorMessage(e.getErrorMessage())
                            .build());
        }

    }
}
