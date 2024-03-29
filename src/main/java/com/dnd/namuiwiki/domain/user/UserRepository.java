package com.dnd.namuiwiki.domain.user;

import com.dnd.namuiwiki.domain.oauth.type.OAuthProvider;
import com.dnd.namuiwiki.domain.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByOauthProviderAndOauthId(OAuthProvider oauthProvider, String oauthId);
    boolean existsByOauthProviderAndOauthId(OAuthProvider oauthProvider, String oauthId);
    Optional<User> findByWikiId(String wikiId);
}
