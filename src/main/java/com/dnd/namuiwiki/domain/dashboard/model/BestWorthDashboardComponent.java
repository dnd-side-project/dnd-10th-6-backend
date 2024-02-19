package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.model.dto.RatioDto;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.statistic.model.RatioStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistics;
import lombok.Getter;

import java.util.List;

@Getter
public class BestWorthDashboardComponent extends DashboardComponent {
    private List<RatioDto> rank;

    public BestWorthDashboardComponent(Statistics statistics) {
        super(DashboardType.BEST_WORTH);
        calculate(statistics);
    }

    @Override
    public void calculate(Statistics statistics) {
        RatioStatistic bestWorth = (RatioStatistic) statistics.getStatisticsByDashboardType(this.dashboardType).get(0);
        Long totalCount = bestWorth.getTotalCount();
        this.rank = bestWorth.getLegends().stream()
                .map(legend -> {
                    int percentage = (int) (legend.getCount() * 100 / totalCount);
                    return new RatioDto(legend.getText(), percentage);
                })
                .toList();
    }

}
