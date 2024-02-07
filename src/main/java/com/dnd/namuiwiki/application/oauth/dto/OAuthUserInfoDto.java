package com.dnd.namuiwiki.application.oauth.dto;

import com.dnd.namuiwiki.application.oauth.type.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthUserInfoDto {

    private OAuthProvider provider;
    private String oAuthId;

}
