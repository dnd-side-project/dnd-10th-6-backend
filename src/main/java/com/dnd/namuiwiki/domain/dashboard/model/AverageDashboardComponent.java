package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import lombok.Getter;

import java.util.List;

@Getter
public class AverageDashboardComponent extends DashboardComponent {
    private final String questionId;
    private long peopleCount;
    private long average;
    private long entireAverage;

    public AverageDashboardComponent(DashboardType dashboardType, List<Statistic> statistics, long entireAverage) {
        super(dashboardType);
        this.entireAverage = entireAverage;
        this.questionId = statistics.get(0).getQuestionId();

        calculate(statistics);
    }

    @Override
    public void calculate(List<Statistic> statistics) {
        AverageStatistic money = (AverageStatistic) statistics.get(0);
        this.peopleCount = money.getTotalCount();
        this.average = money.getAverage();
    }

}
