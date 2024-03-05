package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import lombok.Getter;

@Getter
public class MoneyDashboardComponent extends DashboardComponent {
    private final String questionId;
    private long peopleCount;
    private long average;
    private long entireAverage;

    public MoneyDashboardComponent(Statistics statistics, long entireAverage, String questionId) {
        super(DashboardType.MONEY);
        this.entireAverage = entireAverage;
        this.questionId = questionId;

        calculate(statistics);
    }

    @Override
    public void calculate(Statistics statistics) {
        AverageStatistic money = (AverageStatistic) statistics.getStatisticsByDashboardType(this.dashboardType).get(0);
        this.peopleCount = money.getTotalCount();
        this.average = money.getAverage();
    }

}
