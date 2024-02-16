package com.dnd.namuiwiki.domain.auth;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.auth.dto.*;
import com.dnd.namuiwiki.domain.jwt.JwtAuthorization;
import com.dnd.namuiwiki.domain.jwt.JwtService;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.oauth.OAuthService;
import com.dnd.namuiwiki.domain.oauth.dto.OAuthUserInfoDto;
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
    private final String REFRESH_TOKEN_COOKIE = "refreshToken";
    private final long REFRESH_TOKEN_COOKIE_MAX_AGE = 60 * 60 * 24 * 7;

    private final OAuthService oAuthService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody OAuthLoginRequest request) {
        OAuthUserInfoDto oauthUserInfo = oAuthService.oauthLogin(request.getProvider(), request.getCode());
        OAuthLoginResponse oAuthLoginResponse = userService.login(oauthUserInfo);
        return ResponseDto.setCookie(REFRESH_TOKEN_COOKIE, oAuthLoginResponse.getRefreshToken(), REFRESH_TOKEN_COOKIE_MAX_AGE)
                .body(oAuthLoginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@CookieValue("oauthProvider") String oauthProvider,
                                    @CookieValue("oauthAccessToken") String oauthAccessToken,
                                    @Validated @RequestBody SignUpRequest signUpRequest) {

        SignUpResponse signUpResponse = userService.signUp(oauthProvider, oauthAccessToken, signUpRequest.getNickname());
        return ResponseDto.setCookie(REFRESH_TOKEN_COOKIE, signUpResponse.getRefreshToken(), REFRESH_TOKEN_COOKIE_MAX_AGE)
                .body(signUpResponse);
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
