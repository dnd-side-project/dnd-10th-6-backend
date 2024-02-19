package com.dnd.namuiwiki.domain.oauth;

import com.dnd.namuiwiki.domain.oauth.dto.OAuthUserInfoDto;

public interface OAuthProviderService {
    OAuthUserInfoDto getOAuthUserInfo(String code);
    String getOAuthUserId(String accessToken);
}
