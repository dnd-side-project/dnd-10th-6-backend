package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.statistic.model.Legend;
import com.dnd.namuiwiki.domain.statistic.model.RatioStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistic;
import lombok.Getter;

import java.util.Map;

@Getter
public class BinaryDashboardComponent extends DashboardComponentV2 {
    private final int percentage;

    public BinaryDashboardComponent(Statistic statistic, Question question) {
        super(DashboardType.BINARY, question.getId(), question.getTitle(), question.getName(), question.getDashboardOrder());

        if (!dashboardType.isBinaryType()) {
            throw new IllegalArgumentException("Required BinaryDashboardType");
        }

        RatioStatistic ratioStatistic = (RatioStatistic) statistic;

        this.percentage = getPercentage(ratioStatistic, question);
    }

    private int getPercentage(RatioStatistic ratioStatistic, Question question) {
        Legend trueLegend = ratioStatistic.getLegends().stream()
                .filter(legend -> (boolean) getValue(question, legend))
                .findFirst()
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR));
        return (int) (trueLegend.getCount() * 100 / ratioStatistic.getTotalCount());
    }

    private Object getValue(Question question, Legend legend) {
        Map<String, Option> options = question.getOptions();
        Option option = options.get(legend.getOptionId());
        return option.getValue();
    }

}
