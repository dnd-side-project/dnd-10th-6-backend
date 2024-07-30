package com.dnd.namuiwiki.domain.statistic.model;

import com.dnd.namuiwiki.domain.dashboard.model.dto.RankDto;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class RankStatistic extends Statistic {
    private final Map<String, RankDto> ranks;

    public RankStatistic(String questionId, QuestionName questionName, DashboardType dashboardType, Long totalCount, Map<String, RankDto> ranks) {
        super(questionId, questionName, dashboardType, totalCount);
        this.ranks = ranks;
    }

    public static RankStatistic create(Question question) {
        Map<String, RankDto> rankMap = question.getOptions().values().stream()
                .collect(Collectors.toMap(Option::getId, RankDto::optionToRankDto));
        return new RankStatistic(
                question.getId(),
                question.getName(),
                question.getDashboardType(),
                0L,
                rankMap);
    }

    @Override
    public void updateStatistic(Answer answer) {
        increaseTotalCount();

        if (answer.getType().isOptionList()) {
            updateRankMap(answer);
        }
    }

    private void updateRankMap(Answer answer) {
        // rankMap point update
        List<String> answerList = (List<String>) answer.getAnswer();
        for (int i = 0; i < 5; i++) {
            int point = 5 - i;
            String optionId = answerList.get(i);
            ranks.get(optionId).addPoint(point);
        }

        // rankMap percentage update
        long totalPoint = this.totalCount * (5 + 4 + 3 + 2 + 1);
        ranks.forEach((key, value) -> {
            int percentage = (int) (value.getPoint() / totalPoint);
            value.updatePercentage(percentage);
        });
    }
}
