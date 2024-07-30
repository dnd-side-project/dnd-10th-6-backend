package com.dnd.namuiwiki.domain.statistic.type;

public enum StatisticsType {

    RATIO,
    AVERAGE,
    RANK,
    NONE;

    public boolean isNotNone() {
        return this != NONE;
    }

}
