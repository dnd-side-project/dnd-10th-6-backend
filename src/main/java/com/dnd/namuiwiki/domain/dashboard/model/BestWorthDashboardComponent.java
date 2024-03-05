package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.model.dto.RatioDto;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import lombok.Getter;

import java.util.List;

@Getter
public class BestWorthDashboardComponent extends DashboardComponent {
    private final String questionId;
    private List<RatioDto> rank;

    public BestWorthDashboardComponent(Statistics statistics, String questionId) {
        super(DashboardType.BEST_WORTH);
        this.questionId = questionId;

        calculate(statistics);
    }

    @Override
    public void calculate(Statistics statistics) {
        RatioStatistic bestWorth = (RatioStatistic) statistics.getStatisticsByDashboardType(this.dashboardType).get(0);
        Long totalCount = bestWorth.getTotalCount();

        this.rank = bestWorth.getLegends().stream()
                .map(legend -> {
                    if (totalCount == 0) {
                        return new RatioDto(legend.getText(), 0);
                    }
                    int percentage = (int) (legend.getCount() * 100 / totalCount);
                    return new RatioDto(legend.getText(), percentage);
                })
                .toList();
    }

}
