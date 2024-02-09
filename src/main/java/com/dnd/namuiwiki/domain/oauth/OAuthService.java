package com.dnd.namuiwiki.domain.oauth;

import com.dnd.namuiwiki.domain.oauth.dto.OAuthUserInfoDto;
import com.dnd.namuiwiki.domain.oauth.kakao.OAuthKakaoService;
import com.dnd.namuiwiki.domain.oauth.type.OAuthProvider;
import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OAuthService {
    private final Map<OAuthProvider, OAuthProviderService> providerServiceHandler = new HashMap<>();

    public OAuthService(OAuthKakaoService oAuthKakaoService) {
        providerServiceHandler.put(OAuthProvider.KAKAO, oAuthKakaoService);
    }

    public OAuthUserInfoDto oauthLogin(String provider, String code) {
        OAuthProviderService providerService = providerServiceHandler.get(OAuthProvider.of(provider));
        if (providerService == null) {
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_DATA_ARGUMENT, "지원하지 않는 프로바이더입니다.");
        }
        return providerService.getOAuthUserInfo(code);
    }

}
