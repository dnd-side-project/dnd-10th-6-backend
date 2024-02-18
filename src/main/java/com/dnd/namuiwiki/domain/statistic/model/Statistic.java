package com.dnd.namuiwiki.domain.statistic.model;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
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
        switch (statisticsType) {
            case RATIO:
                return RatioStatistic.create(question);
            case AVERAGE:
                return AverageStatistic.create(question);
            default:
                throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR, "Invalid statistics type");
        }
    }

    public abstract void updateStatistic(Survey.Answer answer);

}
