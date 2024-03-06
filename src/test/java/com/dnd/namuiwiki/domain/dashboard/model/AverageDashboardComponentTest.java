package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.statistic.model.AverageEntireStatistic;
import com.dnd.namuiwiki.domain.statistic.model.entity.PopulationStatistic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AverageDashboardComponentTest {
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

        PopulationStatistic populationStatistic = PopulationStatistic.builder()
                .statistic(new AverageEntireStatistic(entireAverage, totalCount))
                .build();

        // when
        AverageDashboardComponent averageDashboardComponent = new AverageDashboardComponent(DashboardType.MONEY, statistic, populationStatistic);

        // then
        long expectedAverage = 1000;
        long expectedEntireAverage = 2000;
        assertThat(averageDashboardComponent.getEntireAverage()).isEqualTo(expectedEntireAverage);
        assertThat(averageDashboardComponent.getAverage()).isEqualTo(expectedAverage);

    }
}
