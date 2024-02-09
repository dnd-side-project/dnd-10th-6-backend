package com.dnd.namuiwiki.domain.oauth.dto;

import com.dnd.namuiwiki.domain.oauth.type.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthUserInfoDto {

    private OAuthProvider provider;
    private String oAuthId;

}
