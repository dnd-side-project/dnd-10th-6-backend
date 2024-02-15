package com.dnd.namuiwiki.domain.oauth.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserInfoDto {
    private String oAuthId;
    private String nickname;
}
