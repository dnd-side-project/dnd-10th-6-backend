package com.dnd.namuiwiki.application.oauth;

import com.dnd.namuiwiki.application.oauth.dto.OAuthUserInfoDto;

public interface OAuthProviderService {
    OAuthUserInfoDto getOAuthUserInfo(String code);
}
