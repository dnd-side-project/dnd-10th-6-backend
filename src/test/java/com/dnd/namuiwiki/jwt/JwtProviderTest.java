package com.dnd.namuiwiki.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test"})
class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;
    TokenUserInfoDto tokenUserInfoDto = new TokenUserInfoDto("KAKAO", "1234");

    @Test
    void createToken() {
        //when
        assertDoesNotThrow(() -> jwtProvider.createToken(tokenUserInfoDto));
    }

    @Test
    void validateToken() {
        //given
        String token = jwtProvider.createToken(tokenUserInfoDto);

        //when
        assertDoesNotThrow(() -> jwtProvider.validateToken(token));
    }

    @Test
    void parseToken() {
        //given
        String token = jwtProvider.createToken(tokenUserInfoDto);
        Jws<Claims> claims = jwtProvider.validateToken(token);

        //when
        TokenUserInfoDto parsed = jwtProvider.parseToken(claims);
        assertEquals(parsed.getProvider(), tokenUserInfoDto.getProvider());
        assertEquals(parsed.getOAuthId(), tokenUserInfoDto.getOAuthId());
    }
}