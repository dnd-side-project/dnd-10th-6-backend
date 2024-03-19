package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.common.dto.PageableDto;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetAnswersByQuestionResponse {
    private String questionTitle;
    private QuestionName questionName;
    private PageableDto<SingleAnswerWithSurveyDetailDto> answers;
}
