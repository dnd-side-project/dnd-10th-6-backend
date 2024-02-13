package com.dnd.namuiwiki.domain.auth;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.auth.dto.RefreshResponse;
import com.dnd.namuiwiki.domain.jwt.JwtAuthorization;
import com.dnd.namuiwiki.domain.jwt.JwtService;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.oauth.OAuthService;
import com.dnd.namuiwiki.domain.oauth.dto.OAuthUserInfoDto;
import com.dnd.namuiwiki.domain.auth.dto.OAuthLoginRequest;
import com.dnd.namuiwiki.domain.auth.dto.OAuthLoginResponse;
import com.dnd.namuiwiki.common.dto.ResponseDto;
import com.dnd.namuiwiki.domain.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${jwt.authentication-header}")
    private String AUTHENTICATION_HEADER;
    private final String REFRESH_TOKEN_COOKIE = "refreshToken=%s; secure";

    private final OAuthService oAuthService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody OAuthLoginRequest request) {
        OAuthUserInfoDto oauthUserInfo = oAuthService.oauthLogin(request.getProvider(), request.getCode());
        /*
        TODO
        1. 유저 DB에 데이터 저장
        2. jwt 발행
         */
        OAuthLoginResponse oAuthLoginResponse = userService.login(oauthUserInfo);
        return ResponseDto.setCookie(String.format(REFRESH_TOKEN_COOKIE, oAuthLoginResponse.getRefreshToken()))
                .body(oAuthLoginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, @CookieValue("refreshToken") String refreshToken) {
        String accessToken = request.getHeader(AUTHENTICATION_HEADER);

        if (accessToken == null) {
            throw new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_ACCESS_TOKEN);
        }

        if (refreshToken == null) {
            throw new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_REFRESH_TOKEN);
        }

        RefreshResponse refreshResponse = jwtService.reIssueToken(accessToken, refreshToken);
        return ResponseDto.ok(refreshResponse);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(@JwtAuthorization TokenUserInfoDto tokenUserInfoDto) {
        return null;
    }

}
