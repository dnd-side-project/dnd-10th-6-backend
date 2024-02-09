package com.dnd.namuiwiki.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test"})
class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;
    String wikiId = UUID.randomUUID().toString();

    @Value("${jwt.valid-time.access-token}")
    private long accessTokenValidTime;

    @Test
    void createAccessToken() {
        //when
        assertDoesNotThrow(() -> jwtProvider.createAccessToken(wikiId));
    }

    @Test
    void createRefreshToken() {
        //when
        assertDoesNotThrow(() -> jwtProvider.createRefreshToken());
    }

    @Test
    void validateToken() {
        //given
        String token = jwtProvider.createAccessToken(wikiId);

        //when
        assertDoesNotThrow(() -> jwtProvider.validateToken(token));
    }

    @Test
    void parseToken() {
        //given
        String token = jwtProvider.createAccessToken(wikiId);
        Jws<Claims> claims = jwtProvider.validateToken(token);

        //when
        TokenUserInfoDto parsed = jwtProvider.parseToken(claims);
        assertEquals(parsed.getWikiId(), wikiId);
    }
}