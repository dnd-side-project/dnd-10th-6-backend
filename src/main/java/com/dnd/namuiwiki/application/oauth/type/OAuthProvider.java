package com.dnd.namuiwiki.application.oauth.type;

import lombok.Getter;

@Getter
public enum OAuthProvider {
    KAKAO("KAKAO");

    private String name;

    private OAuthProvider(String name) {
        this.name = name;
    }

    public static OAuthProvider of(String provider) {
        return OAuthProvider.valueOf(provider.toUpperCase());
    }

}
