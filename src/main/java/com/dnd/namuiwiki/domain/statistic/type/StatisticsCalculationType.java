package com.dnd.namuiwiki.domain.statistic.type;

public enum StatisticsCalculationType {

    RATIO,
    AVERAGE,
    NONE;

    public boolean isNotNone() {
        return this != NONE;
    }

}
