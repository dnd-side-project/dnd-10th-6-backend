package com.dnd.namuiwiki.domain.oauth.kakao.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoOAuthUserInfoRequest {

    public static String requestUserInfoURL;

    @Value("${oauth.kakao.request-user-url}")
    private void setRequestUserInfoURL(String requestUserInfoURL) {
        KakaoOAuthUserInfoRequest.requestUserInfoURL = requestUserInfoURL;
    }

}
