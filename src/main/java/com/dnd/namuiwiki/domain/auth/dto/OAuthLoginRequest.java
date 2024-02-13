package com.dnd.namuiwiki.domain.auth.dto;

import com.dnd.namuiwiki.domain.oauth.type.OAuthProvider;
import com.dnd.namuiwiki.common.annotation.Enum;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuthLoginRequest {

    @NotEmpty
    @Enum(enumClass = OAuthProvider.class, ignoreCase = true)
    private final String provider;

    @NotEmpty
    private final String code;

}
