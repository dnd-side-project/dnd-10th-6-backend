package com.dnd.namuiwiki.domain.jwt.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenPairDto {
    private final String accessToken;
    private final String refreshToken;
}
