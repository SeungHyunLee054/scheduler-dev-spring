package com.lsh.scheduler_dev.common.security.filter;

import com.lsh.scheduler_dev.common.security.constant.AuthorizationConstant;
import com.lsh.scheduler_dev.common.security.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final List<String> WHITE_LIST = List.of("/members/signup", "/members/signin");
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (!isWhiteList(uri)) {
            String accessToken = getTokenFromCookies(request);
            if (jwtProvider.isTokenExpired(accessToken)) {
                //exception
            }
            setSecurityContext(accessToken);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            //exception
        }

        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals(AuthorizationConstant.AUTHORIZATION_HEADER))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(null);
    }

    private void setSecurityContext(String token) {
        SecurityContextHolder
                .getContext()
                .setAuthentication(jwtProvider.getAuthentication(token));
    }

    private boolean isWhiteList(String uri) {
        return WHITE_LIST.stream().anyMatch(uri::contains);
    }
}
