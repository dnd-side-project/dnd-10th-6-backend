package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SingleAnswerWithSurveyDetailDto {
    private String senderName;
    private Period period;
    private Relation relation;
    // TODO: 작성 날짜 포함해야 함
    // private Date date;
    private Object answer;
    private String reason;
}
