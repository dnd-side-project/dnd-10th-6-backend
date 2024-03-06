package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.model.dto.RatioDto;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import lombok.Getter;

import java.util.List;

@Getter
public class SadDashboardComponent extends DashboardComponent {
    private final String questionId;
    private List<RatioDto> rank;

    public SadDashboardComponent(List<Statistic> statistics) {
        super(DashboardType.SAD);
        this.questionId = statistics.get(0).getQuestionId();

        calculate(statistics);
    }

    @Override
    public void calculate(List<Statistic> statistics) {
        RatioStatistic sad = (RatioStatistic) statistics.get(0);
        Long totalCount = sad.getTotalCount();
        this.rank = sad.getLegends().stream()
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
