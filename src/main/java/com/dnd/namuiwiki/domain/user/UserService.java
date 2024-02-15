package com.dnd.namuiwiki.domain.user;

import com.dnd.namuiwiki.domain.auth.dto.OAuthLoginResponse;
import com.dnd.namuiwiki.domain.jwt.JwtService;
import com.dnd.namuiwiki.domain.oauth.dto.OAuthUserInfoDto;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public OAuthLoginResponse login(OAuthUserInfoDto oAuthUserInfoDto) {
        User user = userRepository.findByOauthProviderAndOauthId(oAuthUserInfoDto.getProvider(), oAuthUserInfoDto.getOAuthId())
                .orElseGet(() -> {
                    User newUser = new User(oAuthUserInfoDto.getProvider(), oAuthUserInfoDto.getOAuthId(), oAuthUserInfoDto.getNickname());
                    return userRepository.save(newUser);
                });
        OAuthLoginResponse oAuthLoginResponse = jwtService.issueTokenPair(user.getWikiId());
        user.setRefreshToken(oAuthLoginResponse.getRefreshToken());
        userRepository.save(user);
        return oAuthLoginResponse;
    }
}
