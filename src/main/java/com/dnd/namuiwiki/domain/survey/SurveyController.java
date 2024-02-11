package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.dto.ResponseDto;
import com.dnd.namuiwiki.domain.survey.dto.CreateSurveyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/surveys")
public class SurveyController {
    private final SurveyService surveyService;
    private final SurveyAnswerService surveyAnswerService;

    @PostMapping
    public ResponseEntity<?> createSurvey(
            @Validated @RequestBody CreateSurveyRequest request
    ) {
        var surveyAnswers = surveyAnswerService.getSurveyAnswers(request.getAnswers());
        var response = surveyService.createSurvey(request, surveyAnswers);
        return ResponseDto.created(response);
    }

}
