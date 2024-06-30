package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SentSurveyDto {
    private String surveyId;
    private WikiType wikiType;
    private Relation relation;
    private Period period;
    private String senderName;
    private LocalDateTime sentAt;

    public static SentSurveyDto from(Survey survey) {
        return new SentSurveyDto(
                survey.getId(),
                survey.getWikiType(),
                survey.getRelation(),
                survey.getPeriod(),
                survey.getOwner().getNickname(),
                survey.getWrittenAt()
        );
    }
}
