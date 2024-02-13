package com.dnd.namuiwiki.domain.user.entity;

import com.dnd.namuiwiki.domain.oauth.type.OAuthProvider;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String wikiId;
    private OAuthProvider oauthProvider;
    private String oauthId;
    private String refreshToken;

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
