package com.dnd.namuiwiki.domain.survey.dto;


import com.dnd.namuiwiki.common.annotation.Enum;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class AnswerDto {

    @NotEmpty
    private String questionId;

    @Enum(enumClass = AnswerType.class)
    private String type;

    @NotEmpty
    private String answer;

    @NotEmpty
    private String reason;

}