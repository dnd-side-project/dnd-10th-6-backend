package com.dnd.namuiwiki.domain.statistic;

import com.dnd.namuiwiki.domain.dashboard.DashboardRepository;
import com.dnd.namuiwiki.domain.dashboard.model.entity.Dashboard;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.statistic.model.Statistic;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class StatisticsServiceTest {

    @Autowired
    StatisticsService statisticsService;

    @Autowired
    DashboardRepository dashboardRepository;

    @BeforeEach
    void beforeEach() {
        dashboardRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자 대시보드 최초 업데이트 시, user와 period/relation 조합의 총 3개의 대시보드가 DB에 존재한다.")
    void createThreeDashboards() {
        // given
        Period period = Period.INFINITE;
        Relation relation = Relation.ELEMENTARY_SCHOOL;
        User owner = User.builder().id("testId").wikiId("wikiId").build();

        Survey survey = Survey.builder()
                .owner(owner)
                .senderName("김철수")
                .period(period)
                .relation(relation)
                .answers(makeAnswerList())
                .build();

        // when
        statisticsService.updateStatistics(survey);

        // then
        boolean dashboard1 = dashboardRepository.existsByUserAndPeriodAndRelation(owner, null, null);
        boolean dashboard2 = dashboardRepository.existsByUserAndPeriodAndRelation(owner, period, null);
        boolean dashboard3 = dashboardRepository.existsByUserAndPeriodAndRelation(owner, null, relation);

        assertThat(dashboard1).isEqualTo(true);
        assertThat(dashboard2).isEqualTo(true);
        assertThat(dashboard3).isEqualTo(true);

    }

    @ParameterizedTest
    @DisplayName("period와 relation 필터 적용하지 않은 대시보드는 설문이 생성되는 수만큼 totalCount가 증가한다.")
    @ValueSource(ints = {5, 10})
    void updateTotalCountBySurveySize(int surveySize) throws InterruptedException {

        // given
        Period period = Period.INFINITE;
        Relation relation = Relation.ELEMENTARY_SCHOOL;
        User owner = User.builder().id("testId").wikiId("wikiId").build();

        Survey survey = Survey.builder()
                .owner(owner)
                .senderName("김철수")
                .period(period)
                .relation(relation)
                .answers(makeAnswerList())
                .build();

        // when
        int numberOfThreads = surveySize - 1;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);

        statisticsService.updateStatistics(survey);
        for (int i = 0; i < numberOfThreads; i++) {
            service.execute(() -> statisticsService.updateStatistics(survey));
        }

        service.shutdown();
        boolean finished = service.awaitTermination(1000, TimeUnit.MINUTES);

        assertTrue(finished);

        // then
        Dashboard dashboard = dashboardRepository.findByUserAndPeriodAndRelation(owner, null, null).orElseThrow();
        Statistic statistic = dashboard.getStatistics().getStatisticsByDashboardType(DashboardType.BEST_WORTH).get(0);

        Long totalCount = statistic.getTotalCount();
        assertThat(totalCount).isEqualTo(surveySize);

    }

    private List<Answer> makeAnswerList() {

        Question question1 = Question.builder()
                .id("questionId1")
                .type(QuestionType.MULTIPLE_CHOICE)
                .dashboardType(DashboardType.BEST_WORTH)
                .options(Map.of("option1", Option.builder().id("option1").build(), "option2", Option.builder().id("option2").build(), "option3", Option.builder().id("option3").build()))
                .name(QuestionName.CORE_VALUE).build();
        Object answer = "option1";

        Question question2 = Question.builder()
                .id("questionId2")
                .type(QuestionType.NUMERIC_CHOICE)
                .dashboardType(DashboardType.MONEY)
                .options(Map.of("option1", Option.builder().id("option1").build(), "option2", Option.builder().id("option2").build(), "option3", Option.builder().id("option3").build()))
                .name(QuestionName.BORROWING_LIMIT).build();
        Object answer2 = 11;

        return List.of(
                Answer.builder().question(question1).type(AnswerType.OPTION).answer(answer).reason("reason").build(),
                Answer.builder().question(question2).type(AnswerType.MANUAL).answer(answer2).reason("reason").build()
        );
    }

}
