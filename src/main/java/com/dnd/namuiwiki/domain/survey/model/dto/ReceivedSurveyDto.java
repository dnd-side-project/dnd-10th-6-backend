package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReceivedSurveyDto {

    private String surveyId;
    private Relation relation;
    private Period period;
    private String senderName;

    public static ReceivedSurveyDto from(Survey survey) {
        return new ReceivedSurveyDto(
                survey.getId(),
                survey.getRelation(),
                survey.getPeriod(),
                survey.getSenderName()
        );
    }

}
