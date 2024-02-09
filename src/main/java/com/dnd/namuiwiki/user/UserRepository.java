package com.dnd.namuiwiki.user;

import com.dnd.namuiwiki.application.oauth.type.OAuthProvider;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByOAuthProviderAndOAuthId(OAuthProvider oAuthProvider, String oAuthId);
}
