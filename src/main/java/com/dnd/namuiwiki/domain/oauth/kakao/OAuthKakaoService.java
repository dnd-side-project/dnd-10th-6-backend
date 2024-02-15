package com.dnd.namuiwiki.domain.oauth.kakao;

import com.dnd.namuiwiki.domain.oauth.OAuthProviderService;
import com.dnd.namuiwiki.domain.oauth.dto.OAuthUserInfoDto;
import com.dnd.namuiwiki.domain.oauth.kakao.dto.*;
import com.dnd.namuiwiki.domain.oauth.type.OAuthProvider;
import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.LinkedHashMap;

@Component
public class OAuthKakaoService implements OAuthProviderService {

    private final String NICKNAME_PROPERTY = "nickname";

    @Override
    public OAuthUserInfoDto getOAuthUserInfo(String code) {
        String accessToken = getAccessToken(code);
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(accessToken);
        return new OAuthUserInfoDto(OAuthProvider.KAKAO, kakaoUserInfoDto.getOAuthId(), kakaoUserInfoDto.getNickname());
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

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) {
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

        String oAuthId = userInfoResponse.getId().toString();
        var properties = (LinkedHashMap<String, String>) userInfoResponse.getProperties();
        String nickname = properties.get(NICKNAME_PROPERTY);

        return new KakaoUserInfoDto(oAuthId, nickname);
    }

}
