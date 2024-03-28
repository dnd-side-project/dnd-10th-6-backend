package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.model.dto.RatioDto;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.statistic.model.Legend;
import com.dnd.namuiwiki.domain.statistic.model.RatioStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class HappyDashboardComponentTest {
    final DashboardType dashboardType = DashboardType.HAPPY;

    @Test
    @DisplayName("범례별 비율을 계산하여 비율이 높은 순서대로 정렬하여 반환한다.")
    void convertStatisticsToDashboardComponentDto() {
        // given
        String questionId = "questionId";
        long totalCount = 5;
        long opt1Count = 3;
        long opt2Count = 2;
        Statistic statistic = new RatioStatistic(
                "questionId",
                QuestionName.HAPPY_BEHAVIOR,
                dashboardType,
                totalCount,
                Map.of("op1", new Legend("op1", "legend1", "legend1", opt1Count),
                        "op2", new Legend("op2", "legend2", "legend2", opt2Count)));

        // when
        HappyDashboardComponent dashboardComponent = new HappyDashboardComponent(new Statistics(Map.of(questionId, statistic)), questionId);

        // then
        int expectedOpt1Percentage = 60;
        int expectedOpt2Percentage = 40;
        assertThat(dashboardComponent.getRank().stream().mapToInt(RatioDto::getPercentage))
                .containsExactly(expectedOpt1Percentage, expectedOpt2Percentage);

    }

    @Test
    @DisplayName("전체 카운트에서 남는 수만큼 직접 입력 비율을 계산하여 반환한다.")
    void calculateManualInputPercentage() {
        // given
        String questionId = "questionId";
        long totalCount = 10;
        long opt1Count = 3;
        long opt2Count = 2;
        Statistic statistic = new RatioStatistic(
                "questionId",
                QuestionName.HAPPY_BEHAVIOR,
                dashboardType,
                totalCount,
                Map.of("op1", new Legend("op1", "legend1", "legend1", opt1Count),
                        "op2", new Legend("op2", "legend2", "legend2", opt2Count),
                        "MANUAL", new Legend("op2", "직접 입력", "직접 입력", 0L)));

        // when
        HappyDashboardComponent dashboardComponent = new HappyDashboardComponent(new Statistics(Map.of(questionId, statistic)), questionId);

        // then
        int expectedOpt1Percentage = 30;
        int expectedOpt2Percentage = 20;
        int expectedManualInputPercentage = 50;
        assertThat(dashboardComponent.getRank().stream().mapToInt(RatioDto::getPercentage))
                .containsExactly(expectedManualInputPercentage, expectedOpt1Percentage, expectedOpt2Percentage);

    }

    @Test
    @DisplayName("DashboardType이 BEST_WORTH가 아닐 경우 예외를 발생시킨다.")
    void throwErrorIfDashboardTypeIsNotBestWorth() {
        // given
        String questionId = "questionId";
        long totalCount = 5;
        long opt1Count = 3;
        long opt2Count = 2;
        Statistic statistic = new RatioStatistic(
                "questionId",
                QuestionName.HAPPY_BEHAVIOR,
                DashboardType.SAD,
                totalCount,
                Map.of("op1", new Legend("op1", "legend1", "legend1", opt1Count),
                        "op2", new Legend("op2", "legend2", "legend2", opt2Count)));

        // when
        // then
        assertThatThrownBy(() -> new HappyDashboardComponent(new Statistics(Map.of(questionId, statistic)), questionId));
    }

}
