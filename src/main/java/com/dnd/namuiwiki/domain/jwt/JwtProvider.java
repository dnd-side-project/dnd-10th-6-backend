package com.dnd.namuiwiki.domain.jwt;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.key}")
    public String key;

    @Value("${jwt.valid-time.access-token}")
    private long accessTokenValidTime;

    @Value("${jwt.valid-time.refresh-token}")
    private long refreshTokenValidTime;
    private final String WIKI_ID = "WIKI_ID";
    private SecretKey secretKey;

    @PostConstruct
    public void createSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String wikiId) {
        Claims claims = Jwts.claims()
                .subject("accessToken")
                .add(WIKI_ID, wikiId)
                .build();
        return createToken(accessTokenValidTime, claims);
    }

    public String createRefreshToken() {
        Claims claims = Jwts.claims()
                .subject("refreshToken")
                .build();
        return createToken(refreshTokenValidTime, claims);
    }

    private String createToken(Long validTime, Claims claims) {
        Date now = new Date();
        return Jwts.builder()
                .issuedAt(now)
                .header()
                .add("typ", "JWT")
                .add("alg", "HS256")
                .and()
                .claims()
                .add(claims)
                .and()
                .expiration(new Date(now.getTime() + validTime))
                .signWith(secretKey)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new ApplicationErrorException(ApplicationErrorType.EXPIRED_TOKEN);
        } catch (JwtException e) {
            throw new ApplicationErrorException(ApplicationErrorType.AUTHENTICATION_FAILED);
        }
    }

    public TokenUserInfoDto parseToken(Claims claims) {
        String wikiId = claims.get(WIKI_ID, String.class);
        if (wikiId == null) {
            throw new ApplicationErrorException(ApplicationErrorType.AUTHENTICATION_FAILED);
        }
        return new TokenUserInfoDto(wikiId);
    }

    public TokenUserInfoDto parseToken(String accessToken) {
        return parseToken(validateToken(accessToken));
    }

    public TokenUserInfoDto parseExpiredToken(String token) {
        try {
            Claims claims = Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return parseToken(claims);
        } catch (ExpiredJwtException e) {
            return parseToken(e.getClaims());
        } catch (JwtException e) {
            throw new ApplicationErrorException(ApplicationErrorType.AUTHENTICATION_FAILED);
        }
    }
}
