package com.dnd.namuiwiki.domain.option.dto;

import com.dnd.namuiwiki.domain.option.entity.Option;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionDto {

    private String id;
    private Object value;
    private String text;
    private String description;

    public static OptionDto from(Option option) {
        return OptionDto.builder()
                .id(option.getId())
                .value(option.getValue())
                .text(option.getText())
                .description(option.getDescription())
                .build();
    }

    public Option toEntity() {
        return Option.builder()
                .id(id)
                .value(value)
                .text(text)
                .build();
    }

}
