package com.dnd.namuiwiki.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenUserInfoDto {
    private String provider;
    private String oAuthId;
}
