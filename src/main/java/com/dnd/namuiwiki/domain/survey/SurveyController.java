package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.dto.ResponseDto;
import com.dnd.namuiwiki.domain.survey.model.dto.CreateSurveyRequest;
import com.dnd.namuiwiki.domain.survey.model.dto.GetSurveyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/surveys")
public class SurveyController {
    private final SurveyService surveyService;
    private final SurveyAnswerService surveyAnswerService;

    @PostMapping
    public ResponseEntity<?> createSurvey(
            @Validated @RequestBody CreateSurveyRequest request,
            @RequestHeader(required = false, value = "X-NAMUIWIKI-TOKEN") String accessToken
    ) {
        var surveyAnswers = surveyAnswerService.getSurveyAnswers(request.getAnswers());
        var response = surveyService.createSurvey(request, surveyAnswers, accessToken);
        return ResponseDto.created(response);
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<?> getSurvey(@PathVariable("surveyId") String surveyId) {
        var response = surveyService.getSurvey(surveyId);
        return ResponseDto.ok(response);
    }

}
