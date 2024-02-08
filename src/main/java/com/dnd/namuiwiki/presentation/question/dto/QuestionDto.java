package com.dnd.namuiwiki.presentation.question.dto;

import com.dnd.namuiwiki.domain.entity.Question;
import com.dnd.namuiwiki.domain.type.QuestionType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QuestionDto {

    private String id;
    private String title;
    private QuestionType type;
    private List<OptionDto> options;

    public static QuestionDto from(Question question) {
        return QuestionDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .type(question.getType())
                .options(question.getOptions().stream().map(OptionDto::from).toList())
                .build();
    }

}
