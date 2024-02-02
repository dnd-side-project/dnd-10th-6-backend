package com.dnd.namuiwiki.application.oauth;

import com.dnd.namuiwiki.application.oauth.dto.OAuthUserInfoDto;
import com.dnd.namuiwiki.application.oauth.kakao.OAuthKakaoService;
import com.dnd.namuiwiki.application.oauth.type.OAuthProvider;
import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final OAuthKakaoService oAuthKakaoService;

    public OAuthUserInfoDto oauthLogin(String provider, String code) {
        switch (OAuthProvider.of(provider)) {
            case KAKAO:
                return oAuthKakaoService.getOAuthUserInfo(code);
            default:
                throw new ApplicationErrorException(ApplicationErrorType.INVALID_DATA_ARGUMENT, "지원하지 않는 프로바이더입니다.");
        }
    }

}
