package com.dnd.namuiwiki.domain.auth.dto;

import com.dnd.namuiwiki.domain.jwt.dto.TokenPairDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "회원가입 응답 body")
@Getter
@RequiredArgsConstructor
public class SignUpResponse {
    private final String accessToken;
    private final String refreshToken;

    public static SignUpResponse from(TokenPairDto tokenPairDto) {
        return new SignUpResponse(tokenPairDto.getAccessToken(), tokenPairDto.getRefreshToken());
    }
}
