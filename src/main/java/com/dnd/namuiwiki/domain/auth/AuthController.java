package com.dnd.namuiwiki.domain.auth;

import com.dnd.namuiwiki.common.annotation.DisableSwaggerSecurity;
import com.dnd.namuiwiki.common.dto.ResponseDto;
import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.auth.dto.OAuthLoginRequest;
import com.dnd.namuiwiki.domain.auth.dto.OAuthLoginResponse;
import com.dnd.namuiwiki.domain.auth.dto.RefreshResponse;
import com.dnd.namuiwiki.domain.auth.dto.SignUpRequest;
import com.dnd.namuiwiki.domain.auth.dto.SignUpResponse;
import com.dnd.namuiwiki.domain.jwt.JwtAuthorization;
import com.dnd.namuiwiki.domain.jwt.JwtService;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.oauth.OAuthService;
import com.dnd.namuiwiki.domain.oauth.dto.OAuthUserInfoDto;
import com.dnd.namuiwiki.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증", description = "Auth API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${jwt.authentication-header}")
    private String AUTHENTICATION_HEADER;
    private final String OAUTH_PROVIDER_COOKIE = "oauthProvider";
    private final String OAUTH_ACCESS_TOKEN_COOKIE = "oauthAccessToken";
    private final String REFRESH_TOKEN_COOKIE = "refreshToken";
    private final long REFRESH_TOKEN_COOKIE_MAX_AGE = 60 * 60 * 24 * 7;

    private final OAuthService oAuthService;
    private final UserService userService;
    private final JwtService jwtService;

    @Operation(summary = "oauth 통한 로그인",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = OAuthLoginResponse.class))),
                    @ApiResponse(responseCode = "404", description = "회원가입이 되지 않은 유저")})
    @DisableSwaggerSecurity
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody OAuthLoginRequest request) {
        OAuthUserInfoDto oauthUserInfo = oAuthService.oauthLogin(request.getProvider(), request.getCode());
        OAuthLoginResponse oAuthLoginResponse = userService.login(oauthUserInfo);
        return ResponseDto.setCookie(REFRESH_TOKEN_COOKIE, oAuthLoginResponse.getRefreshToken(), REFRESH_TOKEN_COOKIE_MAX_AGE)
                .body(oAuthLoginResponse);
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@CookieValue(OAUTH_PROVIDER_COOKIE) String oauthProvider,
                                    @CookieValue(OAUTH_ACCESS_TOKEN_COOKIE) String oauthAccessToken,
                                    @Validated @RequestBody SignUpRequest signUpRequest) {

        SignUpResponse signUpResponse = userService.signUp(oauthProvider, oauthAccessToken, signUpRequest.getNickname());
        return ResponseDto.setCookie(REFRESH_TOKEN_COOKIE, signUpResponse.getRefreshToken(), REFRESH_TOKEN_COOKIE_MAX_AGE)
                .body(signUpResponse);
    }

    @Operation(summary = "access token 재발급",
            responses = {
                    @ApiResponse(responseCode = "200", description = "재발급 성공", content = @Content(schema = @Schema(implementation = RefreshResponse.class))),
                    @ApiResponse(responseCode = "401", description = "refresh token 만료")})
    @DisableSwaggerSecurity
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, @CookieValue(REFRESH_TOKEN_COOKIE) String refreshToken) {
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

    @Operation(hidden = true)
    @GetMapping("/test")
    public ResponseEntity<?> test(@JwtAuthorization TokenUserInfoDto tokenUserInfoDto) {
        return null;
    }

}
