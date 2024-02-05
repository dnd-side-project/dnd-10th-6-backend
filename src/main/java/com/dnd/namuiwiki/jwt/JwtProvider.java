package com.dnd.namuiwiki.jwt;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider implements InitializingBean {

    @Value("${jwt.key}")
    public String key;

    @Value("${jwt.token-valid-time}")
    private long tokenValidTime;
    private SecretKey secretKey;
    private final String OAUTH_PROVIDER_CLAIM = "oAuthProvider";
    private final String OAUTH_ID_CLAIM = "oAuthId";

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(TokenUserInfoDto tokenUserInfoDto) {
        return Jwts.builder()
                .header()
                    .add("typ", "JWT")
                    .add("alg", "HS256")
                .and()
                .claims()
                    .add(OAUTH_PROVIDER_CLAIM, tokenUserInfoDto.getProvider())
                    .add(OAUTH_ID_CLAIM, tokenUserInfoDto.getOAuthId())
                .and()
                .expiration(new Date(new Date().getTime() + this.tokenValidTime))
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
        return new TokenUserInfoDto(claims.get(OAUTH_PROVIDER_CLAIM).toString(), claims.get(OAUTH_ID_CLAIM).toString());
    }
}
