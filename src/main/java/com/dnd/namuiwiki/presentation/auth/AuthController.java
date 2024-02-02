package com.dnd.namuiwiki.presentation.auth;

import com.dnd.namuiwiki.application.oauth.OAuthService;
import com.dnd.namuiwiki.application.oauth.dto.OAuthUserInfoDto;
import com.dnd.namuiwiki.presentation.auth.dto.OAuthLoginRequest;
import com.dnd.namuiwiki.presentation.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final OAuthService oAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody OAuthLoginRequest request) {
        OAuthUserInfoDto oauthUserInfo = oAuthService.oauthLogin(request.getProvider(), request.getCode());
        /*
        TODO
        1. 유저 DB에 데이터 저장
        2. jwt 발행
         */
        return ResponseDto.noContent();
    }

}
