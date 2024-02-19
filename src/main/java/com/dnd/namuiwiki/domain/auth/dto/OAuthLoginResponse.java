package com.dnd.namuiwiki.domain.auth.dto;

import com.dnd.namuiwiki.domain.jwt.dto.TokenPairDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuthLoginResponse {

    private final String accessToken;
    private final String refreshToken;

    public static OAuthLoginResponse from(TokenPairDto tokenPairDto) {
        return new OAuthLoginResponse(tokenPairDto.getAccessToken(), tokenPairDto.getRefreshToken());
    }

}
