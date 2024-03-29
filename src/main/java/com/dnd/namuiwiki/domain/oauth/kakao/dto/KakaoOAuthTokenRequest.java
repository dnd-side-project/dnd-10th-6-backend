package com.dnd.namuiwiki.domain.oauth.kakao.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class KakaoOAuthTokenRequest {

    public static String requestTokenURL;
    private static String grantType;
    private static String clientId;
    private static String redirectUri;
    private static String clientSecret;

    public static MultiValueMap<String, String> from(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", grantType);
        form.add("client_id", clientId);
        form.add("redirect_uri", redirectUri);
        form.add("client_secret", clientSecret);
        form.add("code", code);

        return form;
    }

    @Value("${oauth.kakao.request-token-url}")
    private void setRequestTokenURL(String requestTokenURL) {
        KakaoOAuthTokenRequest.requestTokenURL = requestTokenURL;
    }

    @Value("${oauth.kakao.grant-type}")
    private void setGrantType(String grantType) {
        KakaoOAuthTokenRequest.grantType = grantType;
    }

    @Value("${oauth.kakao.client-id}")
    private void setClientId(String clientId) {
        KakaoOAuthTokenRequest.clientId = clientId;
    }

    @Value("${oauth.kakao.redirect-uri}")
    private void setRedirectUri(String redirectUri) {
        KakaoOAuthTokenRequest.redirectUri = redirectUri;
    }

    @Value("${oauth.kakao.client-secret}")
    private void setClientSecret(String clientSecret) {
        KakaoOAuthTokenRequest.clientSecret = clientSecret;
    }

}
