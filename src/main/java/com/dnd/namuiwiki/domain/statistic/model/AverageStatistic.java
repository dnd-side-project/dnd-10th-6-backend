package com.dnd.namuiwiki.domain.statistic.model;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.statistic.type.DashboardType;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;

public class AverageStatistic extends Statistic {
    private Long totalSum;

    public AverageStatistic(String questionId, DashboardType dashboardType, Long totalCount, Long totalSum) {
        super(questionId, dashboardType, totalCount);
        this.totalSum = totalSum;
    }

    public Long increaseTotalSum(Long sum) {
        return totalSum += sum;
    }

    public static AverageStatistic create(Question question) {
        return new AverageStatistic(
                question.getId(),
                question.getDashboardType(),
                0L,
                0L);
    }

    @Override
    public void updateStatistic(Survey.Answer answer) {
        increaseTotalCount();

        long value = 0L;
        Question question = answer.getQuestion();
        if (answer.getType().isOption()) {
            String optionId = answer.getAnswer().toString();
            Option option = question.getOptions().stream()
                    .filter(op -> op.getId().equals(optionId)).findFirst()
                    .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_OPTION_ID));
            value = Long.parseLong(option.getValue().toString());
        } else if (answer.getType().isManual()) {
            value = Long.parseLong(answer.getAnswer().toString());
        }
        increaseTotalSum(value);
    }

}
