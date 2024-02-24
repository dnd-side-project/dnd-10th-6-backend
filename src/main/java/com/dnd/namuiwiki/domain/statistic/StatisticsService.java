package com.dnd.namuiwiki.domain.statistic;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.dashboard.DashboardRepository;
import com.dnd.namuiwiki.domain.dashboard.model.entity.Dashboard;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.statistic.model.BorrowingLimitEntireStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistics;
import com.dnd.namuiwiki.domain.statistic.model.entity.PopulationStatistic;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final DashboardRepository dashboardRepository;
    private final StatisticsRepository statisticsRepository;

    @Async
    public void updateStatistics(Survey survey) {
        User owner = survey.getOwner();
        Period period = survey.getPeriod();
        Relation relation = survey.getRelation();

        var statisticalAnswers = survey.getAnswers().stream()
                .filter(answer -> answer.getQuestion().getDashboardType().getStatisticsType().isNotNone())
                .toList();

        updateDashboards(owner, period, relation, statisticalAnswers);
        updateBorrowingLimitStatistic(period, relation, statisticalAnswers);

    }

    public PopulationStatistic getPopulationStatistic(Period period, Relation relation, QuestionName questionName) {
        return statisticsRepository
                .findByPeriodAndRelationAndQuestionName(period, relation, questionName)
                .orElseGet(() -> PopulationStatistic.builder()
                        .statistic(new BorrowingLimitEntireStatistic(0L, 0L))
                        .period(period)
                        .questionName(questionName)
                        .relation(relation)
                        .build());
    }

    private void updateDashboards(User owner, Period period, Relation relation, List<Survey.Answer> statisticalAnswers) {
        updateDashboardByCategory(owner, null, null, statisticalAnswers);
        updateDashboardByCategory(owner, period, null, statisticalAnswers);
        updateDashboardByCategory(owner, null, relation, statisticalAnswers);
    }

    private void updateDashboardByCategory(User owner, Period period, Relation relation, List<Survey.Answer> answers) {
        Dashboard dashboard = dashboardRepository.findByUserAndPeriodAndRelation(owner, period, relation)
                .orElseGet(() -> {
                    Dashboard newDashboard = Dashboard.builder()
                            .user(owner)
                            .period(period)
                            .relation(relation)
                            .statistics(Statistics.from(answers.stream().map(Survey.Answer::getQuestion).toList()))
                            .build();
                    return dashboardRepository.save(newDashboard);
                });
        dashboard.updateStatistics(answers);
        dashboardRepository.save(dashboard);
    }

    private void updateBorrowingLimitStatistic(Period period, Relation relation, List<Survey.Answer> answers) {
        Survey.Answer borroingLimitAnswer = answers.stream()
                .filter(answer -> answer.getQuestion().getName() == QuestionName.BORROWING_LIMIT)
                .findFirst()
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_DATA_ARGUMENT));

        long borrowingLimit;
        if (borroingLimitAnswer.getType().isOption()) {
            Option option = borroingLimitAnswer.getQuestion().getOptions().get(borroingLimitAnswer.getAnswer().toString());
            borrowingLimit = (long) option.getValue();
        } else {
            borrowingLimit = (long) borroingLimitAnswer.getAnswer();
        }

        updateBorrowingLimitStatisticByCategory(null, null, borrowingLimit);
        updateBorrowingLimitStatisticByCategory(period, null, borrowingLimit);
        updateBorrowingLimitStatisticByCategory(null, relation, borrowingLimit);
    }

    private void updateBorrowingLimitStatisticByCategory(Period period, Relation relation, long borrowingLimit) {
        PopulationStatistic populationStatistic = getPopulationStatistic(period, relation, QuestionName.BORROWING_LIMIT);

        BorrowingLimitEntireStatistic statistic = (BorrowingLimitEntireStatistic) populationStatistic.getStatistic();
        statistic.updateStatistic(String.valueOf(borrowingLimit));

        populationStatistic.setStatistic(statistic);

        statisticsRepository.save(populationStatistic);
    }

}
