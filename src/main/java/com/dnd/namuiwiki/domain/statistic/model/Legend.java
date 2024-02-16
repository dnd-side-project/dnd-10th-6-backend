package com.dnd.namuiwiki.domain.statistic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Legend {
    private String optionId;
    private String text;
    private Long count;

    public Long increaseCount() {
        return ++count;
    }
}
