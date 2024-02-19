package com.dnd.namuiwiki.domain.jwt;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.auth.dto.OAuthLoginResponse;
import com.dnd.namuiwiki.domain.auth.dto.RefreshResponse;
import com.dnd.namuiwiki.domain.jwt.dto.TokenPairDto;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.user.UserRepository;
import com.dnd.namuiwiki.domain.user.UserService;
import com.dnd.namuiwiki.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public TokenPairDto issueTokenPair(String wikiId) {
        String accessToken = jwtProvider.createAccessToken(wikiId);
        String refreshToken = jwtProvider.createRefreshToken();
        return new TokenPairDto(accessToken, refreshToken);
    }

    public RefreshResponse reIssueToken(String accessToken, String refreshToken) {
        String wikiId = jwtProvider.parseExpiredToken(accessToken).getWikiId();
        jwtProvider.validateToken(refreshToken);

        User user = userRepository.findByWikiId(wikiId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_USER));

        if(!user.getRefreshToken().equals(refreshToken)) {
            throw new ApplicationErrorException(ApplicationErrorType.TOKEN_INTERNAL_ERROR);
        }

        return new RefreshResponse(jwtProvider.createAccessToken(wikiId));
    }
}
