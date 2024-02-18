package com.dnd.namuiwiki.domain.user;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.auth.dto.OAuthLoginResponse;
import com.dnd.namuiwiki.domain.auth.dto.SignUpResponse;
import com.dnd.namuiwiki.domain.jwt.JwtService;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.jwt.dto.TokenPairDto;
import com.dnd.namuiwiki.domain.oauth.OAuthService;
import com.dnd.namuiwiki.domain.oauth.dto.OAuthUserInfoDto;
import com.dnd.namuiwiki.domain.oauth.type.OAuthProvider;
import com.dnd.namuiwiki.domain.survey.SurveyRepository;
import com.dnd.namuiwiki.domain.user.dto.UserProfileDto;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final String COOKIE_OAUTH_ACCESSTOKEN = "oauthAccessToken";
    private final String COOKIE_OAUTH_PROVIDER = "oauthProvider";

    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final JwtService jwtService;
    private final OAuthService oAuthService;

    public OAuthLoginResponse login(OAuthUserInfoDto oAuthUserInfoDto) {
        User user = userRepository.findByOauthProviderAndOauthId(oAuthUserInfoDto.getProvider(), oAuthUserInfoDto.getOAuthId())
                .orElseThrow(() -> {
                    MultiValueMap<String, String> headers = getOauthCookie(oAuthUserInfoDto);
                    return new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_USER, headers);
                });

        TokenPairDto tokenPair = jwtService.issueTokenPair(user.getWikiId());
        user.setRefreshToken(tokenPair.getRefreshToken());
        userRepository.save(user);
        return OAuthLoginResponse.from(tokenPair);
    }

    public SignUpResponse signUp(String oauthProvider, String oauthAccessToken, String nickname) {
        String oAuthUserId = oAuthService.getOAuthUserId(oauthProvider, oauthAccessToken);
        OAuthProvider oAuthProvider = OAuthProvider.of(oauthProvider);

        if (userRepository.existsByOauthProviderAndOauthId(oAuthProvider, oAuthUserId)) {
            throw new ApplicationErrorException(ApplicationErrorType.EXISTING_USER);
        }

        User user = User.builder()
                .oauthProvider(oAuthProvider)
                .oauthId(oAuthUserId)
                .wikiId(UUID.randomUUID().toString())
                .nickname(nickname)
                .build();

        TokenPairDto tokenPair = jwtService.issueTokenPair(user.getWikiId());
        user.setRefreshToken(tokenPair.getRefreshToken());
        userRepository.save(user);
        return SignUpResponse.from(tokenPair);
    }

    private MultiValueMap<String, String> getOauthCookie(OAuthUserInfoDto oAuthUserInfoDto) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        ResponseCookie oauthToken = ResponseCookie.from(COOKIE_OAUTH_ACCESSTOKEN, oAuthUserInfoDto.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 15)
                .build();
        ResponseCookie provider = ResponseCookie.from(COOKIE_OAUTH_PROVIDER, oAuthUserInfoDto.getProvider().getName())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 15)
                .build();

        headers.add(HttpHeaders.SET_COOKIE, oauthToken.toString());
        headers.add(HttpHeaders.SET_COOKIE, provider.toString());
        return headers;
    }


    public UserProfileDto getMyProfile(TokenUserInfoDto tokenUserInfoDto) {
        String wikiId = tokenUserInfoDto.getWikiId();
        User user = userRepository.findByWikiId(wikiId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_USER));

        Long totalSurveyCnt = surveyRepository.countByOwner(user);

        // TODO : profile에 nickname 추가
        return new UserProfileDto(user.getWikiId(), "엽용현", totalSurveyCnt);
    }

}
