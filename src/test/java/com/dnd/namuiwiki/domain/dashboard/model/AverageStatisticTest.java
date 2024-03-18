package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AverageStatisticTest {

    AverageStatistic statistic;
    private final Option option1 = Option.builder().id("op1").value(10).text("legend1").build();
    private final Option option2 = Option.builder().id("op2").value(100).text("legend2").build();
    private final Question question = Question.builder()
            .id("questionId")
            .name(QuestionName.CORE_VALUE)
            .dashboardType(DashboardType.BEST_WORTH)
            .type(QuestionType.MULTIPLE_CHOICE)
            .options(Map.of("op1", option1, "op2", option2))
            .build();

    @BeforeEach
    void beforeEach() {
        long totalCount = 5;
        long average = 1000;
        statistic = new AverageStatistic(
                question.getId(),
                question.getName(),
                question.getDashboardType(),
                totalCount,
                average);
    }

    @Test
    @DisplayName("통계 업데이트 시 totalCount가 1 증가한다.")
    void increaseOneForTotalCount() {
        // given
        Answer answer = new Answer(question, AnswerType.OPTION, "op1", "reason");

        // when
        statistic.updateStatistic(answer);

        // then
        long expectedTotalCount = 6;
        assertThat(statistic.getTotalCount()).isEqualTo(expectedTotalCount);
    }

    @Test
    @DisplayName("옵션 선택 시, 해당 옵션의 value가 추가된 평균으로 업데이트 된다.")
    void updateAverage() {
        // given
        Answer answer = new Answer(question, AnswerType.OPTION, "op1", "reason");

        // when
        statistic.updateStatistic(answer);

        // then
        long expectedAverage = (1000 * 5 + 10) / 6;
        assertThat(statistic.getAverage()).isEqualTo(expectedAverage);
    }

}
