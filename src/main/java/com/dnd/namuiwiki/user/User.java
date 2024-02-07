package com.dnd.namuiwiki.user;

import com.dnd.namuiwiki.application.oauth.type.OAuthProvider;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String wikiId;
    private OAuthProvider oAuthProvider;
    private String oAuthId;

    public User(OAuthProvider oAuthProvider, String oAuthId) {
        this.wikiId = UUID.randomUUID().toString();
        this.oAuthProvider = oAuthProvider;
        this.oAuthId = oAuthId;
    }
}
