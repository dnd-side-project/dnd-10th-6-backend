package com.dnd.namuiwiki.domain.dashboard;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.dashboard.model.BestWorthDashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.CharacterDashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.DashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.HappyDashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.MoneyDashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.SadDashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.Statistics;
import com.dnd.namuiwiki.domain.dashboard.model.dto.DashboardDto;
import com.dnd.namuiwiki.domain.dashboard.model.entity.Dashboard;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.statistic.StatisticsService;
import com.dnd.namuiwiki.domain.statistic.model.BorrowingLimitEntireStatistic;
import com.dnd.namuiwiki.domain.statistic.model.entity.PopulationStatistic;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.UserRepository;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final UserRepository userRepository;
    private final DashboardRepository dashboardRepository;
    private final StatisticsService statisticsService;

    public DashboardDto getDashboard(TokenUserInfoDto tokenUserInfoDto, Period period, Relation relation) {
        validateFilterCategory(period, relation);

        User user = findByWikiId(tokenUserInfoDto.getWikiId());
        Optional<Dashboard> dashboard = dashboardRepository.findByUserAndPeriodAndRelation(user, period, relation);
        if (dashboard.isEmpty()) {
            return null;
        }

        Statistics statistics = dashboard.get().getStatistics();
        return createDashboardDto(period, relation, statistics);
    }

    private DashboardDto createDashboardDto(Period period, Relation relation, Statistics statistics) {
        List<DashboardComponent> dashboardComponents = List.of(
                new BestWorthDashboardComponent(statistics.getStatisticsByDashboardType(DashboardType.BEST_WORTH)),
                new HappyDashboardComponent(statistics.getStatisticsByDashboardType(DashboardType.HAPPY)),
                new SadDashboardComponent(statistics.getStatisticsByDashboardType(DashboardType.SAD)),
                new CharacterDashboardComponent(statistics.getStatisticsByDashboardType(DashboardType.CHARACTER)),
                getMoneyDashboardComponent(statistics, period, relation)
        );
        return new DashboardDto(dashboardComponents);
    }

    private MoneyDashboardComponent getMoneyDashboardComponent(Statistics statistics, Period period, Relation relation) {
        PopulationStatistic populationStatistic = statisticsService.getPopulationStatistic(period, relation, QuestionName.BORROWING_LIMIT);
        BorrowingLimitEntireStatistic statistic = (BorrowingLimitEntireStatistic) populationStatistic.getStatistic();
        long entireAverage = statistic.getBorrowingMoneyLimitEntireAverage();

        return new MoneyDashboardComponent(statistics.getStatisticsByDashboardType(DashboardType.MONEY), entireAverage);
    }

    private void validateFilterCategory(Period period, Relation relation) {
        if (period != null && relation != null) {
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_DATA_ARGUMENT);
        }
    }

    private User findByWikiId(String wikiId) {
        return userRepository.findByWikiId(wikiId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_USER));
    }


    public void updateStatistics(Survey survey) {
        User owner = survey.getOwner();
        Period period = survey.getPeriod();
        Relation relation = survey.getRelation();

        var statisticalAnswers = survey.getAnswers().stream()
                .filter(answer -> answer.getQuestion().getDashboardType().getStatisticsType().isNotNone())
                .toList();

        updateDashboards(owner, period, relation, statisticalAnswers);
    }

    private void updateDashboards(User owner, Period period, Relation relation, List<Answer> statisticalAnswers) {
        updateDashboardByCategory(owner, null, null, statisticalAnswers);
        updateDashboardByCategory(owner, period, null, statisticalAnswers);
        updateDashboardByCategory(owner, null, relation, statisticalAnswers);
    }

    private void updateDashboardByCategory(User owner, Period period, Relation relation, List<Answer> answers) {
        Dashboard dashboard = dashboardRepository.findByUserAndPeriodAndRelation(owner, period, relation)
                .orElseGet(() -> {
                    Dashboard newDashboard = Dashboard.builder()
                            .user(owner)
                            .period(period)
                            .relation(relation)
                            .statistics(Statistics.from(answers.stream().map(Answer::getQuestion).toList()))
                            .build();
                    return dashboardRepository.save(newDashboard);
                });
        dashboard.updateStatistics(answers);
        dashboardRepository.save(dashboard);
    }

}
