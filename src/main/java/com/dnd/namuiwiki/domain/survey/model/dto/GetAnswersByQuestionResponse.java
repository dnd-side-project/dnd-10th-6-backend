package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.common.dto.PageableDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetAnswersByQuestionResponse {
    private String questionTitle;
    private PageableDto<SingleAnswerWithSurveyDetailDto> answers;
}
