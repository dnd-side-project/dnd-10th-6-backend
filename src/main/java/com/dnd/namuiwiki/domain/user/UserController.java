package com.dnd.namuiwiki.domain.user;

import com.dnd.namuiwiki.common.dto.ResponseDto;
import com.dnd.namuiwiki.domain.jwt.JwtAuthorization;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.survey.SurveyService;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.dto.EditUserProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SurveyService surveyService;

    @GetMapping()
    public ResponseEntity<?> getUserPublicProfile(
            @RequestParam String wikiId
    ) {
        var response = userService.getUserPublicProfile(wikiId);
        return ResponseDto.ok(response);
    }

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

    @GetMapping("profile/surveys")
    public ResponseEntity<?> getSentSurveys(
            @JwtAuthorization TokenUserInfoDto tokenUserInfoDto,
            @RequestParam(name = "period", required = false, defaultValue = "TOTAL") Period period,
            @RequestParam(name = "relation", required = false, defaultValue = "TOTAL") Relation relation,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize
    ) {
        var response = surveyService.getSentSurveys(tokenUserInfoDto, period, relation, pageNo, pageSize);
        return ResponseDto.ok(response);
    }

}
