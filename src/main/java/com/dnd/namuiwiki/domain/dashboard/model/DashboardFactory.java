package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardStatisticType;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.statistic.model.entity.PopulationStatistic;

import java.util.List;

public abstract class DashboardFactory {

    public static DashboardComponent create(DashboardType dashboardType, List<Statistic> statistics) {
        DashboardStatisticType type = dashboardType.getDashboardStatisticType();
        switch (type) {
            case RATIO -> {
                validateStatisticsSize(statistics, 1);
                return new RatioDashboardComponent(dashboardType, statistics.get(0));
            }
            case BINARY -> {
                return new BinaryDashboardComponent(dashboardType, statistics);
            }
            default -> throw new IllegalArgumentException("Not supported type: " + type);
        }
    }

    public static DashboardComponent create(DashboardType dashboardType, List<Statistic> statistics, PopulationStatistic populationStatistic) {
        DashboardStatisticType type = dashboardType.getDashboardStatisticType();
        switch (type) {
            case AVERAGE -> {
                validateStatisticsSize(statistics, 1);
                return new AverageDashboardComponent(dashboardType, statistics.get(0), populationStatistic);
            }
            default -> throw new IllegalArgumentException("Not supported type: " + type);
        }
    }

    private static void validateStatisticsSize(List<Statistic> statistics, int size) {
        if (statistics.size() != size) {
            throw new IllegalArgumentException(String.format("This Dashboard type should have only %d statistic", size));
        }
    }

}
