package com.dnd.namuiwiki.domain.dashboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RankDto {
    private String text;
    private int point;
    private int percentage;
    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
