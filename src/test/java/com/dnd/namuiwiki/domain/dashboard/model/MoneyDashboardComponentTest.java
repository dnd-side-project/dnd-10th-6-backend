package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.statistic.model.AverageStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyDashboardComponentTest {
    final DashboardType dashboardType = DashboardType.MONEY;

    @Test
    @DisplayName("AverageStatistic에 설정된 평균 값을 반환한다.")
    void convertStatisticsToDashboardComponentDto() {
        // given
        long totalCount = 5;
        long average = 1000;
        long entireAverage = 2000;

        String questionId = "questionId";

        Statistics statistics = new Statistics(Map.of(questionId, new AverageStatistic(questionId, QuestionName.BORROWING_LIMIT, dashboardType, totalCount, average)));

        // when
        MoneyDashboardComponent dashboardComponent = new MoneyDashboardComponent(statistics, entireAverage, questionId);

        // then
        long expectedAverage = 1000;
        long expectedEntireAverage = 2000;
        assertThat(dashboardComponent.getEntireAverage()).isEqualTo(expectedEntireAverage);
        assertThat(dashboardComponent.getAverage()).isEqualTo(expectedAverage);

    }

    @Test
    @DisplayName("DashboardType이 MONEY가 아닌 경우 예외를 발생한다.")
    void throwExceptionIfDashboardTypeIsNotMoney() {
        // given
        long totalCount = 5;
        long average = 1000;
        long entireAverage = 2000;

        String questionId = "questionId";

        Statistics statistics = new Statistics(Map.of(questionId, new AverageStatistic(questionId, QuestionName.BORROWING_LIMIT, DashboardType.HAPPY, totalCount, average)));

        // when
        // then
        assertThatThrownBy(() -> new MoneyDashboardComponent(statistics, entireAverage, questionId));

    }

}