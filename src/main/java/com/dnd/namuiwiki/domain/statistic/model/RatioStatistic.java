package com.dnd.namuiwiki.domain.statistic.model;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RatioStatistic extends Statistic {
    private final Map<String, Legend> legends;

    public RatioStatistic(
            String questionId,
            QuestionName questionName,
            DashboardType dashboardType,
            Long totalCount,
            Map<String, Legend> legends
    ) {
        super(questionId, questionName, dashboardType, totalCount);
        this.legends = legends;
    }

    public Optional<Legend> getLegend(String optionId) {
        return Optional.ofNullable(legends.get(optionId));
    }

    public List<Legend> getLegends() {
        return legends.values().stream().toList();
    }

    public static RatioStatistic create(Question question) {
        Map<String, Legend> legends = new HashMap<>();
        question.getOptions().forEach((key, value) -> legends.put(key, new Legend(key, 0L)));
        return new RatioStatistic(
                question.getId(),
                question.getName(),
                question.getDashboardType(),
                0L,
                legends);
    }

    @Override
    public void updateStatistic(Answer answer) {
        increaseTotalCount();

        if (answer.getType().isOption()) {
            increaseOptionCount(answer);
        }
    }

    private void increaseOptionCount(Answer answer) {
        Question question = answer.getQuestion();
        String optionId = answer.getAnswer().toString();
        Legend legend = getLegend(optionId)
                .orElseGet(() -> {
                    Option option = question.getOption(optionId);
                    return new Legend(option.getId(), 0L);
                });
        legend.increaseCount();
    }

}
