package com.dnd.namuiwiki.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserProfileDto {
    private final String wikiId;
    private final String name;
    private final Long totalSurveyCnt;
}
