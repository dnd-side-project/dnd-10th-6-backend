package com.dnd.namuiwiki.domain.question.dto;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.option.dto.OptionDto;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

@Schema(description = "질문 응답 body")
@Getter
@Builder
public class QuestionDto {

    private String id;
    private String title;
    private QuestionName name;
    private QuestionType type;
    private boolean reasonRequired;
    private DashboardType dashboardType;
    private Long surveyOrder;
    private WikiType wikiType;
    private List<OptionDto> options;

    public static QuestionDto from(Question question) {
        var questionDto = QuestionDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .name(question.getName())
                .type(question.getType())
                .wikiType(question.getWikiType())
                .reasonRequired(question.isReasonRequired())
                .dashboardType(question.getDashboardType())
                .surveyOrder(question.getSurveyOrder());

        if (question.getOptions() == null) {
            questionDto.options(List.of());
        } else {
            questionDto.options(question.getOptions().values().stream()
                    .sorted(Comparator.comparingInt(Option::getOrder))
                    .map(OptionDto::from)
                    .toList());
        }

        return questionDto.build();
    }

}
