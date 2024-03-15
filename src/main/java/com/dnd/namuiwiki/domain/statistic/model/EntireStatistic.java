package com.dnd.namuiwiki.domain.statistic.model;

import com.dnd.namuiwiki.domain.statistic.type.StatisticsCalculationType;

public abstract class EntireStatistic {
    public abstract void updateStatistic(String... arguments);

    public static EntireStatistic createEmpty(StatisticsCalculationType type) {
        switch (type) {
            case AVERAGE:
                return new AverageEntireStatistic(0L, 0L);
            default:
                throw new IllegalArgumentException("Not supported type: " + type);
        }
    }

}
