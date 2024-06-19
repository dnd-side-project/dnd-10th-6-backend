package com.dnd.namuiwiki.domain.dashboard.type;

import com.dnd.namuiwiki.domain.statistic.type.StatisticsType;

public enum DashboardType {
    BINARY(StatisticsType.RATIO),
    BUBBLE_CHART(StatisticsType.RATIO),
    BAR_CHART(StatisticsType.RATIO),
    BEST_WORTH(StatisticsType.RATIO),
    CHARACTER(StatisticsType.RATIO),
    MONEY(StatisticsType.AVERAGE),
    HAPPY(StatisticsType.RATIO),
    SAD(StatisticsType.RATIO),
    NONE(StatisticsType.NONE);

    private DashboardType(StatisticsType statisticsType) {
        this.statisticsType = statisticsType;
    }

    private final StatisticsType statisticsType;

    public StatisticsType getStatisticsType() {
        return statisticsType;
    }
}
