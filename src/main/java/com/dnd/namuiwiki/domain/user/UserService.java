package com.dnd.namuiwiki.domain.user;

import com.dnd.namuiwiki.domain.auth.dto.OAuthLoginResponse;
import com.dnd.namuiwiki.domain.jwt.JwtService;
import com.dnd.namuiwiki.domain.oauth.dto.OAuthUserInfoDto;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public OAuthLoginResponse login(OAuthUserInfoDto oAuthUserInfoDto) {
        User user = userRepository.findByOauthProviderAndOauthId(oAuthUserInfoDto.getProvider(), oAuthUserInfoDto.getOAuthId())
                .orElseGet(() -> userRepository.save(User.builder()
                        .wikiId(UUID.randomUUID().toString())
                        .oauthProvider(oAuthUserInfoDto.getProvider())
                        .oauthId(oAuthUserInfoDto.getOAuthId())
                        .build()));
        OAuthLoginResponse oAuthLoginResponse = jwtService.issueTokenPair(user.getWikiId());
        user.setRefreshToken(oAuthLoginResponse.getRefreshToken());
        return oAuthLoginResponse;
    }
}
