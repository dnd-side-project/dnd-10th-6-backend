package com.dnd.namuiwiki.domain.auth.dto;

import com.dnd.namuiwiki.common.annotation.Enum;
import com.dnd.namuiwiki.domain.oauth.type.OAuthProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "OAuth 로그인 요청 body")
@Getter
@RequiredArgsConstructor
public class OAuthLoginRequest {

    @Schema(description = "OAuth 프로바이더", implementation = OAuthProvider.class)
    @NotEmpty
    @Enum(enumClass = OAuthProvider.class, ignoreCase = true)
    private final String provider;

    @NotEmpty
    private final String code;

}
