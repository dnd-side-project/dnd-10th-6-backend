package com.dnd.namuiwiki.domain.user.entity;

import com.dnd.namuiwiki.domain.oauth.type.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String wikiId;
    private OAuthProvider oauthProvider;
    private String oauthId;
    private String refreshToken;

    public User(OAuthProvider oAuthProvider, String oAuthId) {
        this.wikiId = UUID.randomUUID().toString();
        this.oauthProvider = oAuthProvider;
        this.oauthId = oAuthId;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
