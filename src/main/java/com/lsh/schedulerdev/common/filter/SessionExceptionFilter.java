package com.lsh.schedulerdev.common.filter;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsh.schedulerdev.common.exception.dto.ErrorResponse;
import com.lsh.schedulerdev.common.filter.exception.FilterException;
import com.lsh.schedulerdev.common.utils.log.LogErrorUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

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
			log.error("예외 발생: {} (ErrorCode: {})", e.getMessage(), e.getErrorCode());
			LogErrorUtils.logError(e);
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
