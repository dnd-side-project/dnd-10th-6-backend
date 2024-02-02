package com.dnd.namuiwiki.application.oauth.kakao;

import com.dnd.namuiwiki.application.oauth.OAuthProviderService;
import com.dnd.namuiwiki.application.oauth.dto.OAuthUserInfoDto;
import com.dnd.namuiwiki.application.oauth.kakao.dto.KakaoOAuthTokenRequest;
import com.dnd.namuiwiki.application.oauth.kakao.dto.KakaoOAuthTokenResponse;
import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OAuthKakaoService implements OAuthProviderService {

    @Override
    public OAuthUserInfoDto getOAuthUserInfo(String code) {
        String accessToken = getAccessToken(code);
        return null;
    }

    private String getAccessToken(String code) {
        KakaoOAuthTokenResponse tokenResponse = RestClient.create().post()
                .uri(KakaoOAuthTokenRequest.requestTokenURL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(KakaoOAuthTokenRequest.from(code))
                .retrieve()
                .toEntity(KakaoOAuthTokenResponse.class)
                .getBody();

        if (tokenResponse == null) {
            throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR);
        }

        return tokenResponse.getAccess_token();
    }

}
