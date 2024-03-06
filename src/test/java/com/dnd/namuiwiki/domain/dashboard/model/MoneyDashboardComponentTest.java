package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MoneyDashboardComponentTest {
    @Test
    @DisplayName("AverageStatistic에 설정된 평균 값을 반환한다.")
    void convertStatisticsToDashboardComponentDto() {
        // given
        long totalCount = 5;
        long average = 1000;
        long entireAverage = 2000;

        Statistic statistic = new AverageStatistic(
                "questionId",
                QuestionName.BORROWING_LIMIT,
                DashboardType.MONEY,
                totalCount,
                average);

        // when
        MoneyDashboardComponent moneyDashboardComponent = new MoneyDashboardComponent(List.of(statistic), entireAverage);

        // then
        long expectedAverage = 1000;
        long expectedEntireAverage = 2000;
        assertThat(moneyDashboardComponent.getEntireAverage()).isEqualTo(expectedEntireAverage);
        assertThat(moneyDashboardComponent.getAverage()).isEqualTo(expectedAverage);

    }
}
