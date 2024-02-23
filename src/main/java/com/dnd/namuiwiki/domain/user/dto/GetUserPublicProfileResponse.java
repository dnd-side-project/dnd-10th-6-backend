package com.dnd.namuiwiki.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "유저 퍼블릭 프로필 응답 body")
@AllArgsConstructor
@Getter
public class GetUserPublicProfileResponse {
    private String nickname;
}
