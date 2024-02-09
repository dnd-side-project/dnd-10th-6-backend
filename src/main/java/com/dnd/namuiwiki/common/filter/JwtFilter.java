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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final String AUTHENTICATION_HEADER = "X-NAMUIWIKI-TOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHENTICATION_HEADER);
        if (authHeader == null) {
            throw new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_ACCESS_TOKEN);
        }
        Jws<Claims> claims = jwtProvider.validateToken(authHeader);
        jwtProvider.parseToken(claims);
        filterChain.doFilter(request, response);
    }
}
