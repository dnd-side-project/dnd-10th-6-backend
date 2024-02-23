package com.dnd.namuiwiki.domain.auth.dto;

import com.dnd.namuiwiki.domain.jwt.dto.TokenPairDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "OAuth 로그인 응답 body")
@Getter
@RequiredArgsConstructor
public class OAuthLoginResponse {

    private final String accessToken;
    private final String refreshToken;

    public static OAuthLoginResponse from(TokenPairDto tokenPairDto) {
        return new OAuthLoginResponse(tokenPairDto.getAccessToken(), tokenPairDto.getRefreshToken());
    }

}
