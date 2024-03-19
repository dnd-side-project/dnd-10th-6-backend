package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.model.dto.RatioDto;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.statistic.model.Legend;
import com.dnd.namuiwiki.domain.statistic.model.RatioStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistics;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class HappyDashboardComponent extends DashboardComponent {
    private List<RatioDto> rank;
    private final String questionId;

    public HappyDashboardComponent(Statistics statistics, String questionId) {
        super(DashboardType.HAPPY);
        this.questionId = questionId;

        calculate(statistics);
    }

    @Override
    public void calculate(Statistics statistics) {
        RatioStatistic happy = (RatioStatistic) statistics.getStatisticsByDashboardType(this.dashboardType).get(0);
        Long totalCount = happy.getTotalCount();

        List<Legend> legends = happy.getLegends();
        int optionPercentage = 0;

        this.rank = new ArrayList<>();

        for (Legend legend : legends) {
            if (totalCount == 0) {
                rank.add(new RatioDto(legend.getText(), 0));
                continue;
            }
            int percentage = (int) (legend.getCount() * 100 / totalCount);
            optionPercentage += percentage;
            rank.add(new RatioDto(legend.getText(), percentage));
        }

        // 직접입력인 legend 인거 찾아서 새로 업데이트
        updateManualLegendPercentage(optionPercentage);
    }

    private void updateManualLegendPercentage(int optionPercentage) {
        Optional<RatioDto> manualLegend = this.rank.stream()
                .filter(legend -> legend.getLegend().contains("직접 입력"))
                .findFirst();
        manualLegend.ifPresent(ratioDto -> ratioDto.setPercentage(100 - optionPercentage));
    }

}
