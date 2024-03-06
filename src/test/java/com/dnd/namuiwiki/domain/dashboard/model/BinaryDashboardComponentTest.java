package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BinaryDashboardComponentTest {
    @Test
    @DisplayName("질문마다 범례별 비율을 계산하여 true인 값이 많을 경우 character value를 true로 반환한다.")
    void convertStatisticsToDashboardComponentDto() {
        // given
        long totalCount = 5;

        Legend legend1 = new Legend("op1", "legend1", true, 3L);
        Legend legend2 = new Legend("op2", "legend2", false, 2L);

        Legend legend3 = new Legend("op3", "legend3", true, 0L);
        Legend legend4 = new Legend("op4", "legend4", false, 5L);

        Statistic statistic1 = new RatioStatistic(
                "questionId",
                QuestionName.MBTI_IMMERSION,
                DashboardType.CHARACTER,
                totalCount,
                Map.of("op1", legend1, "op2", legend2));
        Statistic statistic2 = new RatioStatistic(
                "questionId",
                QuestionName.FRIENDLINESS_LEVEL,
                DashboardType.CHARACTER,
                totalCount,
                Map.of("op3", legend3, "op4", legend4));

        // when
        BinaryDashboardComponent binaryDashboardComponent = new BinaryDashboardComponent(DashboardType.CHARACTER, List.of(statistic1, statistic2));

        // then
        boolean statistic1Result = true;
        boolean statistic2Result = false;
        assertThat(binaryDashboardComponent.getCharacters().stream().map(BinaryDashboardComponent.BinaryMetric::isValue))
                .containsExactly(statistic1Result, statistic2Result);

    }
}
