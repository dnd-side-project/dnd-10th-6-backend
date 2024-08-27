package com.dnd.namuiwiki.domain.statistic.model;

import com.dnd.namuiwiki.domain.option.entity.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Rank {
    private String text;
    private int point;

    public static Rank optionToRankDto(Option option) {
        return new Rank(option.getText(), 0);
    }

    public void addPoint(int point) {
        this.point += point;
    }
}
