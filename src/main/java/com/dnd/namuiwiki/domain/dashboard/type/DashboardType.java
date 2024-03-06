package com.dnd.namuiwiki.domain.dashboard.type;

import com.dnd.namuiwiki.domain.statistic.type.StatisticsType;

public enum DashboardType {

    BEST_WORTH(StatisticsType.RATIO, DashboardStatisticType.RATIO),
    CHARACTER(StatisticsType.RATIO, DashboardStatisticType.BINARY),
    MONEY(StatisticsType.AVERAGE, DashboardStatisticType.AVERAGE),
    HAPPY(StatisticsType.RATIO, DashboardStatisticType.RATIO),
    SAD(StatisticsType.RATIO, DashboardStatisticType.RATIO);

    private DashboardType(StatisticsType statisticsType, DashboardStatisticType dashboardStatisticType) {
        this.statisticsType = statisticsType;
        this.dashboardStatisticType = dashboardStatisticType;
    }

    private final StatisticsType statisticsType;
    private final DashboardStatisticType dashboardStatisticType;

    public StatisticsType getStatisticsType() {
        return statisticsType;
    }

    public DashboardStatisticType getDashboardStatisticType() {
        return dashboardStatisticType;
    }

}
