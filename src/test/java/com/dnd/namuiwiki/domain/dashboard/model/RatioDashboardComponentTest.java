package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.model.dto.RatioDto;
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
class RatioDashboardComponentTest {
    @Test
    @DisplayName("범례별 비율을 계산하여 비율이 높은 순서대로 정렬하여 반환한다.")
    void convertStatisticsToDashboardComponentDto() {
        // given
        long totalCount = 5;
        long opt1Count = 3;
        long opt2Count = 2;
        Statistic statistic = new RatioStatistic(
                "questionId",
                QuestionName.CORE_VALUE,
                DashboardType.BEST_WORTH,
                totalCount,
                Map.of("op1", new Legend("op1", "legend1", "legend1", opt1Count),
                        "op2", new Legend("op2", "legend2", "legend2", opt2Count)));

        // when
        RatioDashboardComponent ratioDashboardComponent = new RatioDashboardComponent(DashboardType.BEST_WORTH, List.of(statistic));

        // then
        int expectedOpt1Percentage = 60;
        int expectedOpt2Percentage = 40;
        assertThat(ratioDashboardComponent.getRank().stream().mapToInt(RatioDto::getPercentage))
                .containsExactly(expectedOpt1Percentage, expectedOpt2Percentage);

    }
}
