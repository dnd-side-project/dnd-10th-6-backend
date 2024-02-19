package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.statistic.model.AverageStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistics;
import lombok.Getter;

@Getter
public class MoneyDashboardComponent extends DashboardComponent {
    private long peopleCount;
    private long moneySum;
    private long average;

    public MoneyDashboardComponent(Statistics statistics) {
        super(DashboardType.MONEY);
        calculate(statistics);
    }

    @Override
    public void calculate(Statistics statistics) {
        AverageStatistic money = (AverageStatistic) statistics.getStatisticsByDashboardType(this.dashboardType)
                .getFirst();
        this.peopleCount = money.getTotalCount();
        this.moneySum = money.getTotalSum();
    }

}
