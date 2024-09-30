package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.domain.option.entity.Option;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SimpleAnswerDto {
    private final String text;
    private final Object value;
    private final String optionName;

    public static SimpleAnswerDto of(Option option) {
        return new SimpleAnswerDto(option.getText(), option.getValue(), option.getName());
    }
}
