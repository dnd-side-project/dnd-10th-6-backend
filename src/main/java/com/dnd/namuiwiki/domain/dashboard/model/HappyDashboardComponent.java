package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.model.dto.RatioDto;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import lombok.Getter;

import java.util.List;

@Getter
public class HappyDashboardComponent extends DashboardComponent {
    private List<RatioDto> rank;
    private final String questionId;

    public HappyDashboardComponent(List<Statistic> statistics) {
        super(DashboardType.HAPPY);
        this.questionId = statistics.get(0).getQuestionId();

        calculate(statistics);
    }

    @Override
    public void calculate(List<Statistic> statistics) {
        RatioStatistic happy = (RatioStatistic) statistics.get(0);
        Long totalCount = happy.getTotalCount();
        this.rank = happy.getLegends().stream()
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
