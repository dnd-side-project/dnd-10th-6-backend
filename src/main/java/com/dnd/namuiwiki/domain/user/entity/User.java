package com.dnd.namuiwiki.domain.user.entity;

import com.dnd.namuiwiki.domain.oauth.type.OAuthProvider;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String wikiId;
    private OAuthProvider oAuthProvider;
    private String oAuthId;
    private String refreshToken;

    public User(OAuthProvider oAuthProvider, String oAuthId) {
        this.wikiId = UUID.randomUUID().toString();
        this.oAuthProvider = oAuthProvider;
        this.oAuthId = oAuthId;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
