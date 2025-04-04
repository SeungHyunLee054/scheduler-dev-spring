package com.lsh.schedulerdev.common.filter;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsh.schedulerdev.common.filter.exception.FilterException;
import com.lsh.schedulerdev.common.response.CommonResponse;
import com.lsh.schedulerdev.common.utils.LogUtils;

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
		} catch (FilterException filterException) {
			LogUtils.logError(filterException);

			response.setStatus(filterException.getHttpStatus().value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");
			objectMapper.writeValue(response.getWriter(),
				CommonResponse.from(filterException.getErrorCode()));
		}

	}
}
