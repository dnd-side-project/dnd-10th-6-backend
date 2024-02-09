package com.dnd.namuiwiki.domain.jwt;

import com.dnd.namuiwiki.domain.auth.dto.OAuthLoginResponse;
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
