package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.model.dto.RatioDto;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.statistic.model.RatioStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistics;
import lombok.Getter;

import java.util.List;

@Getter
public class SadDashboardComponent extends DashboardComponent {
    private List<RatioDto> rank;

    public SadDashboardComponent(Statistics statistics) {
        super(DashboardType.HAPPY);
        calculate(statistics);
    }

    @Override
    public void calculate(Statistics statistics) {
        RatioStatistic sad = (RatioStatistic) statistics.getStatisticsByDashboardType(this.dashboardType)
                .getFirst();
        Long totalCount = sad.getTotalCount();
        this.rank = sad.getLegends().stream()
                .map(legend -> {
                    int percentage = (int) (legend.getCount() * 100 / totalCount);
                    return new RatioDto(legend.getText(), percentage);
                })
                .toList();
    }

}
