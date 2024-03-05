package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.common.util.ArithmeticUtils;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import lombok.Getter;

@Getter
public class AverageStatistic extends Statistic {
    private long average;

    public AverageStatistic(
            String questionId,
            QuestionName questionName,
            DashboardType dashboardType,
            Long totalCount,
            Long average
    ) {
        super(questionId, questionName, dashboardType, totalCount);
        this.average = average;
    }

    @Override
    public void updateStatistic(Answer answer) {
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

        updateAverage(value);
        increaseTotalCount();
    }

    public void updateAverage(Long newNumber) {
        average = ArithmeticUtils.calculateAverage(totalCount, average, newNumber);
    }

    protected static AverageStatistic create(Question question) {
        return new AverageStatistic(
                question.getId(),
                question.getName(),
                question.getDashboardType(),
                0L,
                0L);
    }

    private long getValueFromManualAnswer(Answer answer) {
        long value;
        value = Long.parseLong(answer.getAnswer().toString());
        return value;
    }

    private long getValueFromOption(Answer answer, Question question) {
        long value;
        String optionId = answer.getAnswer().toString();
        Option option = question.getOption(optionId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_OPTION_ID));
        value = Long.parseLong(option.getValue().toString());
        return value;
    }

}
