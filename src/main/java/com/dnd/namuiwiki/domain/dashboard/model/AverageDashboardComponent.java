package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.statistic.model.AverageEntireStatistic;
import com.dnd.namuiwiki.domain.statistic.model.entity.PopulationStatistic;
import lombok.Getter;

@Getter
public class AverageDashboardComponent extends DashboardComponent {
    private final String questionId;
    private final long peopleCount;
    private final long average;
    private final long entireAverage;

    public AverageDashboardComponent(DashboardType dashboardType, Statistic statistic, PopulationStatistic populationStatistic) {
        super(dashboardType);
        AverageEntireStatistic entireStatistic = (AverageEntireStatistic) populationStatistic.getStatistic();
        AverageStatistic averageStatistic = (AverageStatistic) statistic;

        this.entireAverage = entireStatistic.getEntireAverage();
        this.questionId = statistic.getQuestionId();
        this.peopleCount = averageStatistic.getTotalCount();
        this.average = averageStatistic.getAverage();
    }

}
