package com.dnd.namuiwiki.domain.dashboard.type;

import com.dnd.namuiwiki.domain.statistic.type.StatisticsCalculationType;

public enum DashboardType {

    BEST_WORTH(StatisticsCalculationType.RATIO, DashboardStatisticType.RATIO),
    CHARACTER(StatisticsCalculationType.RATIO, DashboardStatisticType.BINARY),
    MONEY(StatisticsCalculationType.AVERAGE, DashboardStatisticType.AVERAGE),
    HAPPY(StatisticsCalculationType.RATIO, DashboardStatisticType.RATIO),
    SAD(StatisticsCalculationType.RATIO, DashboardStatisticType.RATIO);

    private DashboardType(StatisticsCalculationType statisticsCalculationType, DashboardStatisticType dashboardStatisticType) {
        this.statisticsCalculationType = statisticsCalculationType;
        this.dashboardStatisticType = dashboardStatisticType;
    }

    private final StatisticsCalculationType statisticsCalculationType;
    private final DashboardStatisticType dashboardStatisticType;

    public StatisticsCalculationType getStatisticsCalculationType() {
        return statisticsCalculationType;
    }

    public DashboardStatisticType getDashboardStatisticType() {
        return dashboardStatisticType;
    }

}
