package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.model.dto.RatioDto;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.statistic.model.Legend;
import com.dnd.namuiwiki.domain.statistic.model.RatioStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistic;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
public class RatioDashboardComponent extends DashboardComponentV2 {
    private List<RatioDto> rank;

    public RatioDashboardComponent(DashboardType dashboardType, Statistic statistic, Question question) {
        super(dashboardType, question.getId(), question.getTitle(), question.getName(), question.getDashboardOrder());

        if (!dashboardType.isRatioType()) {
            throw new IllegalArgumentException("Required RatioDashboardType");
        }

        calculate((RatioStatistic) statistic, question);
    }

    private void calculate(RatioStatistic statistic, Question question) {
        Long totalCount = statistic.getTotalCount();

        List<Legend> legends = statistic.getLegends();
        int optionPercentage = 0;

        this.rank = new ArrayList<>();

        for (Legend legend : legends) {
            if (totalCount == 0) {
                rank.add(new RatioDto(getText(question, legend), 0));
                continue;
            }
            int percentage = (int) (legend.getCount() * 100 / totalCount);
            optionPercentage += percentage;
            rank.add(new RatioDto(getText(question, legend), percentage));
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

    private String getText(Question question, Legend legend) {
        Map<String, Option> options = question.getOptions();
        Option option = options.get(legend.getOptionId());
        return option.getText();
    }

}
