package com.dnd.namuiwiki.domain.statistic.model;

import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.statistic.type.DashboardType;
import com.dnd.namuiwiki.domain.statistic.type.StatisticsType;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Statistic {
    private String questionId;
    private DashboardType dashboardType;
    private Long totalCount;

    public Long increaseTotalCount() {
        return ++totalCount;
    }

    public static Statistic create(Question question, StatisticsType statisticsType) {
        if (statisticsType == StatisticsType.RATIO) {
            return RatioStatistic.create(question);
        } else {
            return AverageStatistic.create(question);
        }
    }

    public abstract void updateStatistic(Survey.Answer answer);

}
