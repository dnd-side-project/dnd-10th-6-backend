package com.dnd.namuiwiki.common.filter;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final List<String> excludeUrlPatterns;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final String HTTP_METHOD_OPTIONS = "OPTIONS";
    private final String AUTHENTICATION_HEADER = "X-NAMUIWIKI-TOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getMethod().equals(HTTP_METHOD_OPTIONS)) {
            String authHeader = request.getHeader(AUTHENTICATION_HEADER);
            if (authHeader == null) {
                throw new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_ACCESS_TOKEN);
            }
            Jws<Claims> claims = jwtProvider.validateToken(authHeader);
            request.setAttribute("claims", claims);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return excludeUrlPatterns.stream()
                .anyMatch(p -> antPathMatcher.match(p, request.getServletPath()));
    }
}
