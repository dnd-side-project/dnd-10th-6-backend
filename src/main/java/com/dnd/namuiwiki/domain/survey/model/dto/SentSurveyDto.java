package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SentSurveyDto {
    private String surveyId;
    private Relation relation;
    private Period period;
    private String senderName;
    private LocalDateTime sentAt;

    public static SentSurveyDto from(Survey survey) {
        return new SentSurveyDto(
                survey.getId(),
                survey.getRelation(),
                survey.getPeriod(),
                survey.getSenderName(),
                survey.getWrittenAt()
        );
    }
}
