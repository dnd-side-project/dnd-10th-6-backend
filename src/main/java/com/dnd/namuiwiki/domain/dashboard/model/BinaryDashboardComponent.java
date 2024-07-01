package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.statistic.model.Legend;
import com.dnd.namuiwiki.domain.statistic.model.RatioStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistic;
import lombok.Getter;

@Getter
public class BinaryDashboardComponent extends DashboardComponentV2 {
    private final String questionId;
    private final String questionTitle;
    private final QuestionName questionName;
    private final int percentage;

    public BinaryDashboardComponent(Statistic statistic, Question question) {
        super(DashboardType.BINARY);

        RatioStatistic ratioStatistic = (RatioStatistic) statistic;

        this.questionId = question.getId();
        this.questionTitle = question.getTitle();
        this.questionName = question.getName();
        this.percentage = getPercentage(ratioStatistic);
    }

    private int getPercentage(RatioStatistic ratioStatistic) {
        Legend trueLegend = ratioStatistic.getLegends().stream()
                .filter(legend -> (boolean) legend.getValue())
                .findFirst()
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR));
        return (int) (trueLegend.getCount() * 100 / ratioStatistic.getTotalCount());
    }

}
