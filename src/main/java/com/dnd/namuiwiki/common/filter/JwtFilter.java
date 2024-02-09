package com.dnd.namuiwiki.common.filter;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtProvider jwtProvider;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final String AUTHENTICATION_HEADER = "X-NAMUIWIKI-TOKEN";

    @Value("${jwt.permit-uri}")
    private List<String> permittedURIs;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (permittedURIs.stream().noneMatch(p -> antPathMatcher.match(p, request.getRequestURI()))) {
            String authHeader = request.getHeader(AUTHENTICATION_HEADER);
            if (authHeader == null) {
                throw new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_ACCESS_TOKEN);
            }
            Jws<Claims> claims = jwtProvider.validateToken(authHeader);
            jwtProvider.parseToken(claims);
        }
        filterChain.doFilter(request, servletResponse);
    }
}
