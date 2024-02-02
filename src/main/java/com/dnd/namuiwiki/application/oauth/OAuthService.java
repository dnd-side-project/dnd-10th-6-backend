package com.dnd.namuiwiki.application.oauth;

import com.dnd.namuiwiki.application.oauth.dto.KakaoOAuthTokenRequest;
import com.dnd.namuiwiki.application.oauth.dto.KakaoOAuthTokenResponse;
import com.dnd.namuiwiki.application.oauth.type.OAuthProvider;
import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.presentation.auth.dto.OAuthLoginResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class OAuthService {

    public OAuthLoginResponse oauthLogin(String provider, String code) {
        switch (OAuthProvider.of(provider)) {
            case KAKAO:
                return oauthKakaoLogin(code);
            default:
                throw new ApplicationErrorException(ApplicationErrorType.INVALID_DATA_ARGUMENT, "지원하지 않는 프로바이더입니다.");
        }
    }


    private OAuthLoginResponse oauthKakaoLogin(String code) {
        ResponseEntity<KakaoOAuthTokenResponse> response = RestClient.create().post()
                .uri(KakaoOAuthTokenRequest.requestTokenURL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(KakaoOAuthTokenRequest.from(code))
                .retrieve()
                .toEntity(KakaoOAuthTokenResponse.class);
        System.out.println(response.getBody().getAccess_token());

        /**
         * TODO JWT 발급 후 반환
         */
        return null;// new OAuthLoginResponse(null, null);
    }

}
