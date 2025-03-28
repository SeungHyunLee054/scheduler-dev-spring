package com.lsh.scheduler_dev.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class SignInFilter extends OncePerRequestFilter {
    private static final List<String> WHITE_LIST = List.of("/members/signup", "/members/signin");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException
    {

        String uri = request.getRequestURI();
        if (!isWhiteList(uri)) {
            HttpSession session = request.getSession(false);
            if (session != null||session.getAttribute("sessionKey") == null) {
                //exception
            }

        }

        filterChain.doFilter(request, response);
    }

    private boolean isWhiteList(String uri) {
        return WHITE_LIST.stream().anyMatch(uri::contains);
    }
}