package com.dnd.namuiwiki.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.valid-time.access-token}")
    private long accessTokenValidTime;

    @Value("${jwt.valid-time.refresh-token}")
    private long refreshTokenValidTime;

    private final JwtProvider jwtProvider;

    public LoginResponseDto issueToken(TokenUserInfoDto tokenUserInfoDto) {
        String accessToken = jwtProvider.createToken(accessTokenValidTime, tokenUserInfoDto);
        String refreshToken = jwtProvider.createToken(refreshTokenValidTime, tokenUserInfoDto);
        return new LoginResponseDto(accessToken, refreshToken);
    }
}
