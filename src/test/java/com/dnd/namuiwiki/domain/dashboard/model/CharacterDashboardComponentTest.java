package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.statistic.model.Legend;
import com.dnd.namuiwiki.domain.statistic.model.RatioStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CharacterDashboardComponentTest {
    final DashboardType dashboardType = DashboardType.CHARACTER;

    @Test
    @DisplayName("질문마다 범례별 비율을 계산하여 true인 값이 많을 경우 character value를 true로 반환한다.")
    void convertStatisticsToDashboardComponentDto() {
        // given
        long totalCount = 5;

        Legend legend1 = new Legend("op1", "legend1", true, 3L);
        Legend legend2 = new Legend("op2", "legend2", false, 2L);

        Legend legend3 = new Legend("op3", "legend3", true, 0L);
        Legend legend4 = new Legend("op4", "legend4", false, 5L);

        Statistics statistics = new Statistics(Map.of(
                "questionId1", new RatioStatistic("questionId1", QuestionName.MBTI_IMMERSION, dashboardType, totalCount, Map.of("op1", legend1, "op2", legend2)),
                "questionId2", new RatioStatistic("questionId2", QuestionName.FRIENDLINESS_LEVEL, dashboardType, totalCount, Map.of("op3", legend3, "op4", legend4))
        ));

        // when
        CharacterDashboardComponent characterDashboardComponent = new CharacterDashboardComponent(statistics);

        // then
        Optional<CharacterDashboardComponent.Character> result1 = characterDashboardComponent.getCharacters().stream().filter(c -> c.getQuestionId().equals("questionId1")).findFirst();
        boolean expectedResult1 = true;
        assertThat(result1.isPresent()).isEqualTo(true);
        assertThat(result1.get().isValue()).isEqualTo(expectedResult1);

        Optional<CharacterDashboardComponent.Character> result2 = characterDashboardComponent.getCharacters().stream().filter(c -> c.getQuestionId().equals("questionId2")).findFirst();
        boolean expectedResult2 = false;
        assertThat(result2.isPresent()).isEqualTo(true);
        assertThat(result2.get().isValue()).isEqualTo(expectedResult2);


    }

    @Test
    @DisplayName("DashboardType이 CHARACTER가 아닌 경우 반환되는 리스트의 길이는 0이다.")
    void throwExceptionIsDashboardTypeIsNotCharacter() {
        // given
        long totalCount = 5;

        Legend legend1 = new Legend("op1", "legend1", true, 3L);
        Legend legend2 = new Legend("op2", "legend2", false, 2L);

        Statistics statistics = new Statistics(Map.of(
                "questionId1", new RatioStatistic("questionId1", QuestionName.MBTI_IMMERSION, DashboardType.SAD, totalCount, Map.of("op1", legend1, "op2", legend2))
        ));

        // when
        CharacterDashboardComponent characterDashboardComponent = new CharacterDashboardComponent(statistics);

        // then
        assertThat(characterDashboardComponent.getCharacters().size()).isEqualTo(0);

    }

}
