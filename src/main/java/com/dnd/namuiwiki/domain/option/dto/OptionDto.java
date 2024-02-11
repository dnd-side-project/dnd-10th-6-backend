package com.dnd.namuiwiki.domain.option.dto;

import com.dnd.namuiwiki.domain.option.entity.Option;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionDto {

    private String id;
    private Object content;

    public static OptionDto from(Option option) {
        return OptionDto.builder()
                .id(option.getId())
                .content(option.getContent())
                .build();
    }

    public Option toEntity() {
        return Option.builder()
                .id(id)
                .content(content)
                .build();
    }
}
