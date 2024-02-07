package com.dnd.namuiwiki.user;

import com.dnd.namuiwiki.application.oauth.dto.OAuthUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User login(OAuthUserInfoDto oAuthUserInfoDto) {
        return userRepository.findByOAuthProviderAndOAuthId()
                .orElseGet(() -> {
                    User newUser = new User(oAuthUserInfoDto.getProvider(), oAuthUserInfoDto.getOAuthId());
                    return userRepository.save(newUser);
                });
    }
}
