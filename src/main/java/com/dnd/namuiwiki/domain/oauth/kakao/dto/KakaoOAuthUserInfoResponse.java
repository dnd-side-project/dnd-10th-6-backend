package com.dnd.namuiwiki.domain.oauth.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class KakaoOAuthUserInfoResponse {

    private Long id;
    private Boolean has_signed_up;
    private String connected_at;
    private String synched_at;
    private String properties;
    private Object kakao_account;
    private Object for_partner;

}