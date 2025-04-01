package com.lsh.schedulerdev.common.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.web.filter.OncePerRequestFilter;

import com.lsh.schedulerdev.common.constants.SessionConstants;
import com.lsh.schedulerdev.common.filter.exception.FilterException;
import com.lsh.schedulerdev.common.filter.exception.FilterExceptionCode;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SignInFilter extends OncePerRequestFilter {
	private static final List<String> WHITE_LIST = List.of("/members/signup",
		"/members/signin",
		"/resources",
		"/swagger-ui",
		"/v3/api-docs",
		"/swagger-resources",
		"/webjars");

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		String uri = request.getRequestURI();
		if (!isWhiteList(uri)) {
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute(SessionConstants.AUTHORIZATION) == null) {
				throw new FilterException(FilterExceptionCode.INVALID_SESSION);
			}
		}

		filterChain.doFilter(request, response);
	}

	private boolean isWhiteList(String uri) {
		return WHITE_LIST.stream().anyMatch(uri::contains);
	}
}
