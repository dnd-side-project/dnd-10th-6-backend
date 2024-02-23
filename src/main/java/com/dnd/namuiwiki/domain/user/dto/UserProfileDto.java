package com.dnd.namuiwiki.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "유저 프로필")
@Getter
@RequiredArgsConstructor
public class UserProfileDto {
    private final String wikiId;
    private final String name;
    private final Long totalSurveyCnt;
}
