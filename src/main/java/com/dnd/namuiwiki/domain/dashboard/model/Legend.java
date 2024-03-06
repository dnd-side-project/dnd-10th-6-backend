package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.option.entity.Option;
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

    public static Legend from(Option option) {
        return new Legend(option.getId(), option.getText(), option.getValue(), 0L);
    }
}
