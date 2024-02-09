package com.dnd.namuiwiki.jwt;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider implements InitializingBean {

    @Value("${jwt.key}")
    public String key;

    @Value("${jwt.valid-time.access-token}")
    private long accessTokenValidTime;

    @Value("${jwt.valid-time.refresh-token}")
    private long refreshTokenValidTime;
    private final String WIKI_ID = "WIKI_ID";
    private SecretKey secretKey;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String wikiId) {
        return Jwts.builder()
                .header()
                    .add("typ", "JWT")
                    .add("alg", "HS256")
                .and()
                .claims()
                    .add(WIKI_ID, wikiId)
                .and()
                .expiration(new Date(new Date().getTime() + accessTokenValidTime))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken() {
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .add("alg", "HS256")
                .and()
                .expiration(new Date(new Date().getTime() + refreshTokenValidTime))
                .signWith(secretKey)
                .compact();
    }

    public Jws<Claims> validateToken(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (JwtException e) {
            throw new ApplicationErrorException(ApplicationErrorType.AUTHENTICATION_FAILED);
        }
    }

    public TokenUserInfoDto parseToken(Jws<Claims> jwt) {
        Claims claims = jwt.getPayload();
        return new TokenUserInfoDto(claims.get(WIKI_ID).toString());
    }
}
