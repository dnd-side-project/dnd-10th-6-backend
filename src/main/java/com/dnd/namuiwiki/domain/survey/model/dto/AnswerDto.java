package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.common.annotation.Enum;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "질문별 답변")
@Getter
@Builder
public class AnswerDto {

    @NotEmpty
    private String questionId;

    @Enum(enumClass = AnswerType.class)
    private String type;

    @NotEmpty
    private Object answer;

    private String reason;

}
