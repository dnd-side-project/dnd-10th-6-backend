package com.dnd.namuiwiki.application.oauth.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class KakaoOAuthTokenResponse {

    private String token_type;
    private String access_token;
    private String id_token;
    private Long expires_in;
    private String refresh_token;
    private Long refresh_token_expires_in;
    private String scope;

}