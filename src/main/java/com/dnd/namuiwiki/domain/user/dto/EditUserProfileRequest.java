package com.dnd.namuiwiki.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class EditUserProfileRequest {

    @NotEmpty
    @Size(min = 2, max = 6, message = "닉네임의 길이는 2 이상 6 이하이어야 합니다.")
    private String nickname;

}
