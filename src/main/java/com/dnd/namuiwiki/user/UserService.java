package com.dnd.namuiwiki.user;

import com.dnd.namuiwiki.application.oauth.dto.OAuthUserInfoDto;
import com.dnd.namuiwiki.jwt.JwtService;
import com.dnd.namuiwiki.presentation.auth.dto.OAuthLoginResponse;
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
        User user = userRepository.findByOAuthProviderAndOAuthId()
                .orElseGet(() -> {
                    User newUser = new User(oAuthUserInfoDto.getProvider(), oAuthUserInfoDto.getOAuthId());
                    return userRepository.save(newUser);
                });
        OAuthLoginResponse oAuthLoginResponse = jwtService.issueTokenPair(user.getWikiId());
        user.setRefreshToken(oAuthLoginResponse.getRefreshToken());
        return oAuthLoginResponse;
    }
}
