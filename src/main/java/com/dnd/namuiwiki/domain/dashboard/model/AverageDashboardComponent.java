package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.statistic.model.AverageStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistic;
import lombok.Getter;

@Getter
public class AverageDashboardComponent extends DashboardComponentV2 {
    private final long peopleCount;
    private final long average;
    private final long entireAverage;

    public AverageDashboardComponent(DashboardType dashboardType, Statistic statistic, long entireAverage, Question question) {
        super(dashboardType, question.getId(), question.getTitle(), question.getName());

        if (!dashboardType.isAverageType()) {
            throw new IllegalArgumentException("Required AverageDashboardType");
        }

        this.entireAverage = entireAverage;

        AverageStatistic averageStatistic = (AverageStatistic) statistic;
        this.peopleCount = averageStatistic.getTotalCount();
        this.average = averageStatistic.getAverage();
    }

}
