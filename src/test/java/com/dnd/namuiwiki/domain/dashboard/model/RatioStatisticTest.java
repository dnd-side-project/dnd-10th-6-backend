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

class RatioStatisticTest {

    RatioStatistic statistic;
    private final Option option1 = Option.builder().id("op1").value("legend1").text("legend1").build();
    private final Option option2 = Option.builder().id("op2").value("legend2").text("legend2").build();
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
        long opt1Count = 3;
        long opt2Count = 2;
        statistic = new RatioStatistic(
                question.getId(),
                question.getName(),
                question.getDashboardType(),
                totalCount,
                Map.of("op1", new Legend(option1.getId(), option1.getText(), option1.getValue(), opt1Count),
                        "op2", new Legend(option2.getId(), option2.getText(), option2.getValue(), opt2Count)));
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
    @DisplayName("통계 업데이트 시 선택한 옵션 count가 1 증가한다.")
    void increaseOneForSelectedLegendCount() {
        // given
        Answer answer = new Answer(question, AnswerType.OPTION, "op1", "reason");

        // when
        statistic.updateStatistic(answer);

        // then
        long expectedOpt1Count = 4;
        long expectedOpt2Count = 2;
        assertThat(statistic.getLegend("op1").get().getCount()).isEqualTo(expectedOpt1Count);
        assertThat(statistic.getLegend("op2").get().getCount()).isEqualTo(expectedOpt2Count);
    }

}
