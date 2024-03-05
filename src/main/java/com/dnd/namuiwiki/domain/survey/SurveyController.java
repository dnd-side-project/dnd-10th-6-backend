package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.dto.ResponseDto;
import com.dnd.namuiwiki.domain.jwt.JwtAuthorization;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.survey.model.dto.CreateSurveyRequest;
import com.dnd.namuiwiki.domain.survey.model.dto.CreateSurveyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "설문", description = "Survey API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/surveys")
public class SurveyController {
    private final SurveyService surveyService;

    @Operation(summary = "설문 생성", responses = {
            @ApiResponse(responseCode = "201", description = "설문 생성 성공", content = @Content(schema = @Schema(implementation = CreateSurveyResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> createSurvey(
            @Validated @RequestBody CreateSurveyRequest request,
            @RequestHeader(required = false, value = "X-NAMUIWIKI-TOKEN") String accessToken
    ) {
        var response = surveyService.createSurvey(request, accessToken);
        return ResponseDto.created(response);
    }

    @GetMapping
    public ResponseEntity<?> getReceivedSurveys(
            @JwtAuthorization TokenUserInfoDto tokenUserInfoDto,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize
    ) {
        var response = surveyService.getReceivedSurveys(tokenUserInfoDto, pageNo, pageSize);
        return ResponseDto.ok(response);
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<?> getSurvey(@PathVariable("surveyId") String surveyId) {
        var response = surveyService.getSurvey(surveyId);
        return ResponseDto.ok(response);
    }

}
