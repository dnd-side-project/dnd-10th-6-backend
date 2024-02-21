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

import java.util.List;

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
        jwtFilterBean.setFilter(new JwtFilter(jwtProvider, List.of(permittedURIs), AUTHENTICATION_HEADER));
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
