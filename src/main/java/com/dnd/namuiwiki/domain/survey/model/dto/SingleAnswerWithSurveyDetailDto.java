package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SingleAnswerWithSurveyDetailDto {
    private String senderName;
    private Period period;
    private Relation relation;
    private LocalDateTime createdAt;
    private Object answer;
    private String reason;
    private String optionName;
    private WikiType wikiType;

    public static class SingleAnswerWithSurveyDetailDtoBuilder {
        public SingleAnswerWithSurveyDetailDtoBuilder optionName(Question question, Answer surveyAnswer) {
            String optionName = null;

            if (question.getType().isChoiceType()) {
                if (surveyAnswer.getType().isOption()) {
                    optionName = question.getOption(surveyAnswer.getAnswer().toString())
                            .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_OPTION_ID))
                            .getName();
                } else {
                    optionName = question.getOptions().values().stream()
                            .filter(option -> option.getName().contains("MANUAL"))
                            .findFirst()
                            .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_OPTION_ID))
                            .getName();
                }
            }
            this.optionName = optionName;
            return this;
        }
    }

}
