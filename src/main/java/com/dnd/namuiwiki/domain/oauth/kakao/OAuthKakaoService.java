package com.dnd.namuiwiki.domain.oauth.kakao;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.oauth.OAuthProviderService;
import com.dnd.namuiwiki.domain.oauth.dto.OAuthUserInfoDto;
import com.dnd.namuiwiki.domain.oauth.kakao.dto.KakaoOAuthTokenRequest;
import com.dnd.namuiwiki.domain.oauth.kakao.dto.KakaoOAuthTokenResponse;
import com.dnd.namuiwiki.domain.oauth.kakao.dto.KakaoOAuthUserInfoRequest;
import com.dnd.namuiwiki.domain.oauth.kakao.dto.KakaoOAuthUserInfoResponse;
import com.dnd.namuiwiki.domain.oauth.type.OAuthProvider;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OAuthKakaoService implements OAuthProviderService {

    @Override
    public OAuthUserInfoDto getOAuthUserInfo(String code) {
        String accessToken = getAccessToken(code);
        String oauthUserId = getOAuthUserId(accessToken);
        return new OAuthUserInfoDto(OAuthProvider.KAKAO, oauthUserId, accessToken);
    }

    @Override
    public String getOAuthUserId(String accessToken) {
        KakaoOAuthUserInfoResponse userInfoResponse = RestClient.create().post()
                .uri(KakaoOAuthUserInfoRequest.requestUserInfoURL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(KakaoOAuthUserInfoResponse.class)
                .getBody();

        if (userInfoResponse == null) {
            throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR);
        }

        return userInfoResponse.getId().toString();
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
