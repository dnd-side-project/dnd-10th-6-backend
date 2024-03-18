package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class ReceivedSurveyDto {

    private String surveyId;
    private Relation relation;
    private Period period;
    private String senderName;
    private String senderWikiId;

    public static ReceivedSurveyDto from(Survey survey) {
        String senderWikiId = null;
        User sender = survey.getSender();
        if (sender != null) {
            senderWikiId = sender.getWikiId();
        }

        return new ReceivedSurveyDto(
                survey.getId(),
                survey.getRelation(),
                survey.getPeriod(),
                survey.getSenderName(),
                senderWikiId
        );
    }

}
