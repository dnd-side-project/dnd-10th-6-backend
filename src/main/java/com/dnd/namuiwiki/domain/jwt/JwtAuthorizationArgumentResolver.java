package com.dnd.namuiwiki.domain.jwt;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.user.UserRepository;
import com.dnd.namuiwiki.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtAuthorization.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request != null) {
            Claims claims = (Claims) request.getAttribute("claims");
            TokenUserInfoDto tokenUserInfoDto = jwtProvider.parseToken(claims);
            String wikiId = tokenUserInfoDto.getWikiId();
            User user = userRepository.findByWikiId(wikiId)
                    .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_AUTHORITY_DATA));
            return new TokenUserInfoDto(user.getWikiId());
        }
        throw new ApplicationErrorException(ApplicationErrorType.TOKEN_INTERNAL_ERROR);
    }
}
