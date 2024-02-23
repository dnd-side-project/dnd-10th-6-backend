package com.dnd.namuiwiki.domain.survey.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "설문 생성 응답 body")
@RequiredArgsConstructor
@Getter
public class CreateSurveyResponse {
    private final String surveyId;
}
