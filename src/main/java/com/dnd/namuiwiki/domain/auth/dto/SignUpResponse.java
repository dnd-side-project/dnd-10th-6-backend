package com.dnd.namuiwiki.domain.auth.dto;

import com.dnd.namuiwiki.domain.jwt.dto.TokenPairDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignUpResponse {
    private final String accessToken;
    private final String refreshToken;

    public static SignUpResponse from(TokenPairDto tokenPairDto) {
        return new SignUpResponse(tokenPairDto.getAccessToken(), tokenPairDto.getRefreshToken());
    }
}
