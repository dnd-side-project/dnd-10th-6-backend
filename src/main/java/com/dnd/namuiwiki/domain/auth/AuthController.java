package com.dnd.namuiwiki.domain.auth;

import com.dnd.namuiwiki.domain.jwt.JwtAuthorization;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.oauth.OAuthService;
import com.dnd.namuiwiki.domain.oauth.dto.OAuthUserInfoDto;
import com.dnd.namuiwiki.domain.auth.dto.OAuthLoginRequest;
import com.dnd.namuiwiki.domain.auth.dto.OAuthLoginResponse;
import com.dnd.namuiwiki.common.dto.ResponseDto;
import com.dnd.namuiwiki.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final OAuthService oAuthService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody OAuthLoginRequest request) {
        OAuthUserInfoDto oauthUserInfo = oAuthService.oauthLogin(request.getProvider(), request.getCode());
        /*
        TODO
        1. 유저 DB에 데이터 저장
        2. jwt 발행
         */
        OAuthLoginResponse oAuthLoginResponse = userService.login(oauthUserInfo);
        return ResponseDto.ok(oAuthLoginResponse);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(@JwtAuthorization TokenUserInfoDto tokenUserInfoDto) {
        return null;
    }

}
