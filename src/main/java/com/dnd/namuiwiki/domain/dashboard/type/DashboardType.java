package com.dnd.namuiwiki.domain.dashboard.type;

import com.dnd.namuiwiki.domain.statistic.type.StatisticsCalculationType;

public enum DashboardType {

    BEST_WORTH(StatisticsCalculationType.RATIO, DashboardStatisticType.RATIO, AnalysisType.USER),
    CHARACTER(StatisticsCalculationType.RATIO, DashboardStatisticType.BINARY, AnalysisType.USER),
    MONEY(StatisticsCalculationType.AVERAGE, DashboardStatisticType.AVERAGE, AnalysisType.POPULATION),
    HAPPY(StatisticsCalculationType.RATIO, DashboardStatisticType.RATIO, AnalysisType.USER),
    SAD(StatisticsCalculationType.RATIO, DashboardStatisticType.RATIO, AnalysisType.USER);

    private DashboardType(
            StatisticsCalculationType statisticsCalculationType,
            DashboardStatisticType dashboardStatisticType,
            AnalysisType analysisType
    ) {
        this.statisticsCalculationType = statisticsCalculationType;
        this.dashboardStatisticType = dashboardStatisticType;
        this.analysisType = analysisType;
    }

    private final StatisticsCalculationType statisticsCalculationType;
    private final DashboardStatisticType dashboardStatisticType;
    private final AnalysisType analysisType;

    public StatisticsCalculationType getStatisticsCalculationType() {
        return statisticsCalculationType;
    }

    public DashboardStatisticType getDashboardStatisticType() {
        return dashboardStatisticType;
    }

    public AnalysisType getAnalysisType() {
        return analysisType;
    }

}
