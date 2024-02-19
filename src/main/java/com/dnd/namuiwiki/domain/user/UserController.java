package com.dnd.namuiwiki.domain.user;

import com.dnd.namuiwiki.common.dto.ResponseDto;
import com.dnd.namuiwiki.domain.jwt.JwtAuthorization;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.user.dto.EditUserProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getMyProfile(
            @JwtAuthorization TokenUserInfoDto tokenUserInfoDto
    ) {
        var response = userService.getMyProfile(tokenUserInfoDto);
        return ResponseDto.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> editMyProfile(
            @JwtAuthorization TokenUserInfoDto tokenUserInfoDto,
            @RequestBody EditUserProfileRequest request
    ) {
        userService.editMyProfile(tokenUserInfoDto, request);
        return ResponseDto.noContent();
    }

}
