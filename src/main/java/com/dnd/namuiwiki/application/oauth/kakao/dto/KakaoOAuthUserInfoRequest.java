package com.dnd.namuiwiki.application.oauth.kakao.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoOAuthUserInfoRequest {

    public static String requestUserInfoURL;

    @Value("${oauth.kakao.request-user-url}")
    public void setRequestUserInfoURL(String requestUserInfoURL) {
        KakaoOAuthUserInfoRequest.requestUserInfoURL = requestUserInfoURL;
    }

}
