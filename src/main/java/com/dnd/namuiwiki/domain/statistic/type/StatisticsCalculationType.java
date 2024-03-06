package com.dnd.namuiwiki.domain.statistic.type;

public enum StatisticsCalculationType {

    RATIO,
    AVERAGE,
    NONE;

    public boolean isNotNone() {
        return this != NONE;
    }

    public boolean isRatio() {
        return this == RATIO;
    }

    public boolean isAverage() {
        return this == AVERAGE;
    }

}
