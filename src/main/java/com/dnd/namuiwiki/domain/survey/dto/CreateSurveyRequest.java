package com.dnd.namuiwiki.domain.survey.dto;

import com.dnd.namuiwiki.common.annotation.Enum;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateSurveyRequest {

    @NotEmpty
    private String owner;

    private String sender;

    @Size(min = 2, max = 6)
    private String senderName;

    private Boolean isAnonymous;

    @Enum(enumClass = Period.class, ignoreCase = true)
    private String period;

    @Enum(enumClass = Relation.class, ignoreCase = true)
    private String relation;

    @NotEmpty
    private List<AnswerDto> answers;

    @Getter
    public static class AnswerDto {

        @NotEmpty
        private String questionId;

        @Enum(enumClass = AnswerType.class)
        private String type;

        @NotEmpty
        private String answer;

        @NotEmpty
        private String reason;

    }

}
