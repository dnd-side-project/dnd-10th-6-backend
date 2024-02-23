package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
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
    private String answer;
    private String reason;
}
