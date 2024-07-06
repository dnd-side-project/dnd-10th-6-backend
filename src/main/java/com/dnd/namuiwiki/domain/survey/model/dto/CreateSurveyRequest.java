package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.common.annotation.Enum;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "설문 생성 요청 body")
@Getter
@Builder
public class CreateSurveyRequest {

    @Enum(enumClass = WikiType.class, ignoreCase = true)
    private String wikiType;

    @NotEmpty
    private String owner;

    @Size(min = 2, max = 6)
    private String senderName;

    @Enum(enumClass = Period.class, ignoreCase = true)
    private String period;

    @Enum(enumClass = Relation.class, ignoreCase = true)
    private String relation;

    @NotEmpty
    private List<AnswerDto> answers;

}
