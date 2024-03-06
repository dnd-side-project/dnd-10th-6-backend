package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.model.dto.RatioDto;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import lombok.Getter;

import java.util.List;

@Getter
public class RatioDashboardComponent extends DashboardComponent {
    private final String questionId;
    private List<RatioDto> rank;

    public RatioDashboardComponent(DashboardType dashboardType, Statistic statistic) {
        super(dashboardType);
        RatioStatistic ratioStatistic = (RatioStatistic) statistic;

        this.questionId = ratioStatistic.getQuestionId();
        initiate(ratioStatistic);
    }

    public void initiate(RatioStatistic statistic) {
        Long totalCount = statistic.getTotalCount();

        this.rank = statistic.getLegends().stream()
                .map(legend -> {
                    if (totalCount == 0) {
                        return new RatioDto(legend.getText(), 0);
                    }
                    int percentage = (int) (legend.getCount() * 100 / totalCount);
                    return new RatioDto(legend.getText(), percentage);
                })
                .sorted((a, b) -> b.getPercentage() - a.getPercentage())
                .toList();
    }

}
