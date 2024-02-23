package com.dnd.namuiwiki.domain.survey.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetAnswersByQuestionResponse {
    private String questionTitle;
    private List<SingleAnswerWithSurveyDetailDto> answers;
}
