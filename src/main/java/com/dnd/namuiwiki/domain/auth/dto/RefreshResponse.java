package com.dnd.namuiwiki.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "access token 재발급 body")
@Getter
@AllArgsConstructor
public class RefreshResponse {
    private String accessToken;
}
