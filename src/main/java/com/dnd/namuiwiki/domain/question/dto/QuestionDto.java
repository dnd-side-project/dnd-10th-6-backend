package com.dnd.namuiwiki.domain.question.dto;

import com.dnd.namuiwiki.domain.option.dto.OptionDto;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
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

    public Question toEntity() {
        return Question.builder()
                .id(id)
                .title(title)
                .type(type)
                .options(options.stream().map(OptionDto::toEntity).toList())
                .build();
    }
}
