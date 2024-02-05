package com.dnd.namuiwiki.jwt;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
}
