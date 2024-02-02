package com.dnd.namuiwiki.presentation.auth.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OAuthLoginResponse {

    private final String accessToken;
    private final String refreshToken;

}
