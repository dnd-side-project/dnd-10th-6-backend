package com.dnd.namuiwiki.domain.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Legend {
    private String optionId;
    private String text;
    private Object value;
    private Long count;

    public Long increaseCount() {
        return ++count;
    }
}
