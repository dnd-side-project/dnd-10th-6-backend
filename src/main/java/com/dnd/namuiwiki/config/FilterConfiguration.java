package com.dnd.namuiwiki.config;

import com.dnd.namuiwiki.common.filter.JwtExceptionHandlerFilter;
import com.dnd.namuiwiki.common.filter.JwtFilter;
import com.dnd.namuiwiki.domain.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class FilterConfiguration {

    @Value("${spring.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Value("${jwt.permit-uri}")
    private String[] permittedURIs;

    @Value("${jwt.authentication-header}")
    private String AUTHENTICATION_HEADER;

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> jwtFilterBean = new FilterRegistrationBean<>();
        Map<String, String> excludeUrlPatterns = new HashMap<>();

        for (int i = 0; i < permittedURIs.length; i++) {
            String[] split = permittedURIs[i].split(":");
            String method = split[0];
            String path = split[1];

            excludeUrlPatterns.put(path, method);
        }

        jwtFilterBean.setFilter(new JwtFilter(jwtProvider, excludeUrlPatterns, AUTHENTICATION_HEADER));
        jwtFilterBean.setOrder(2);
        return jwtFilterBean;
    }

    @Bean
    public FilterRegistrationBean<JwtExceptionHandlerFilter> jwtExceptionHandlerFilter() {
        FilterRegistrationBean<JwtExceptionHandlerFilter> jwtExceptionHandlerFilterBean = new FilterRegistrationBean<>();
        jwtExceptionHandlerFilterBean.setFilter(new JwtExceptionHandlerFilter(objectMapper, allowedOrigins, List.of(permittedURIs)));
        jwtExceptionHandlerFilterBean.setOrder(1);
        return jwtExceptionHandlerFilterBean;
    }
}
