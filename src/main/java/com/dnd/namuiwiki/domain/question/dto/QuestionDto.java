package com.dnd.namuiwiki.domain.question.dto;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.option.dto.OptionDto;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class QuestionDto {

    private String id;
    private String title;
    private QuestionName name;
    private QuestionType type;
    private DashboardType dashboardType;
    private Long surveyOrder;
    private List<OptionDto> options;

    public static QuestionDto from(Question question) {
        var questionDto = QuestionDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .name(question.getName())
                .type(question.getType())
                .dashboardType(question.getDashboardType())
                .surveyOrder(question.getSurveyOrder());

        if (question.getOptions() == null) {
            questionDto.options(List.of());
        } else {
            questionDto.options(question.getOptions().values().stream().map(OptionDto::from).toList());
        }

        return questionDto.build();
    }

    public Question toEntity() {
        if (id == null) {
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_QUESTION_ID);
        }
        Map<String, Option> questionOptions = new HashMap<>();
        options.forEach(option -> questionOptions.put(option.getId(), option.toEntity()));
        return Question.builder()
                .id(id)
                .title(title)
                .name(name)
                .type(type)
                .dashboardType(dashboardType)
                .surveyOrder(surveyOrder)
                .options(questionOptions)
                .build();
    }
}
