package com.dnd.namuiwiki.jwt;

import com.dnd.namuiwiki.presentation.auth.dto.OAuthLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {


    private final JwtProvider jwtProvider;

    public OAuthLoginResponse issueTokenPair(String wikiId) {
        String accessToken = jwtProvider.createAccessToken(wikiId);
        String refreshToken = jwtProvider.createRefreshToken();
        return new OAuthLoginResponse(accessToken, refreshToken);
    }
}
