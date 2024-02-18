package com.dnd.namuiwiki.domain.statistic.model;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
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
        long value;
        Question question = answer.getQuestion();
        switch (answer.getType()) {
            case OPTION:
                value = getValueFromOption(answer, question);
                break;
            case MANUAL:
                value = getValueFromManualAnswer(answer);
                break;
            default:
                throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR, "Invalid statistics type");
        }

        increaseTotalCount();
        increaseTotalSum(value);
    }

    private long getValueFromManualAnswer(Survey.Answer answer) {
        long value;
        value = Long.parseLong(answer.getAnswer().toString());
        return value;
    }

    private long getValueFromOption(Survey.Answer answer, Question question) {
        long value;
        String optionId = answer.getAnswer().toString();
        Option option = question.getOption(optionId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_OPTION_ID));
        value = Long.parseLong(option.getValue().toString());
        return value;
    }

}
