package com.dnd.namuiwiki.domain.dashboard.model.dto;

import com.dnd.namuiwiki.domain.option.entity.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankDto {
    private String text;
    private int point;
    private int percentage;

    public static RankDto optionToRankDto(Option option) {
        return new RankDto(option.getText(), 0, 0);
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public void updatePercentage(int percentage) {
        this.percentage = percentage;
    }
}
