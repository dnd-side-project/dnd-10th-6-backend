package com.dnd.namuiwiki.domain.dashboard;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.dashboard.model.AverageDashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.BinaryDashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.DashboardComponentV2;
import com.dnd.namuiwiki.domain.dashboard.model.RankDashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.RatioDashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.dto.DashboardDto;
import com.dnd.namuiwiki.domain.dashboard.model.entity.Dashboard;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.question.QuestionRepository;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.statistic.StatisticsService;
import com.dnd.namuiwiki.domain.statistic.model.BorrowingLimitEntireStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistics;
import com.dnd.namuiwiki.domain.statistic.model.entity.PopulationStatistic;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.UserRepository;
import com.dnd.namuiwiki.domain.user.entity.User;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final UserRepository userRepository;
    private final DashboardRepository dashboardRepository;
    private final DashboardInternalProxyService dashboardInternalProxyService;
    private final StatisticsService statisticsService;
    private final QuestionRepository questionRepository;

    public DashboardDto getDashboard(TokenUserInfoDto tokenUserInfoDto, WikiType wikiType, Period period, Relation relation) {
        validateFilterCategory(period, relation);

        User user = findByWikiId(tokenUserInfoDto.getWikiId());
        Optional<Dashboard> dashboard = dashboardRepository.findByUserAndWikiTypeAndPeriodAndRelation(user, wikiType, period, relation);
        return dashboard.map(value -> convertToDto(value.getStatistics(), period, relation)).orElse(null);
    }

    public void updateDashboards(Survey survey) {
        User owner = survey.getOwner();
        WikiType wikiType = survey.getWikiType();
        Period period = survey.getPeriod();
        Relation relation = survey.getRelation();

        var answers = survey.getAnswers().stream()
                .filter(answer -> answer.getQuestion().getDashboardType().getStatisticsType().isNotNone())
                .toList();

        dashboardInternalProxyService.updateDashboard(owner, wikiType, null, null, answers);
        dashboardInternalProxyService.updateDashboard(owner, wikiType, period, null, answers);
        dashboardInternalProxyService.updateDashboard(owner, wikiType, null, relation, answers);
    }

    private DashboardDto convertToDto(Statistics statistics, Period period, Relation relation) {
        List<Question> questions = questionRepository.findAll();

        List<DashboardComponentV2> dashboardComponents = statistics.get().stream()
                .map(statistic -> {
                    Question question = questions.stream()
                            .filter(q -> q.getId().equals(statistic.getQuestionId()))
                            .findFirst()
                            .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_QUESTION_ID));
                    DashboardType dashboardType = statistic.getDashboardType();
                    if (dashboardType.isBinaryType()) {
                        return new BinaryDashboardComponent(statistic, question);
                    } else if (dashboardType.isAverageType()) {
                        long entireAverage = getEntireAverage(period, relation, question.getName());
                        return new AverageDashboardComponent(dashboardType, statistic, entireAverage, question);
                    } else if (dashboardType.isRatioType()) {
                        return new RatioDashboardComponent(dashboardType, statistic, question);
                    } else if (dashboardType.isRankType()) {
                        return new RankDashboardComponent(dashboardType, statistic, question);
                    } else {
                        throw new ApplicationErrorException(ApplicationErrorType.INVALID_DASHBOARD_TYPE);
                    }
                }).toList();

        return new DashboardDto(dashboardComponents);
    }

    private long getEntireAverage(Period period, Relation relation, QuestionName questionName) {
        PopulationStatistic populationStatistic = statisticsService.getPopulationStatistic(period, relation, questionName);
        BorrowingLimitEntireStatistic statistic = (BorrowingLimitEntireStatistic) populationStatistic.getStatistic();
        return statistic.getBorrowingMoneyLimitEntireAverage();
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

}
