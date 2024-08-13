package com.dnd.namuiwiki.domain.dashboard;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.dashboard.model.*;
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
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.UserRepository;
import com.dnd.namuiwiki.domain.user.entity.User;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final UserRepository userRepository;
    private final DashboardRepository dashboardRepository;
    private final StatisticsService statisticsService;
    private final QuestionRepository questionRepository;

    public DashboardDto getDashboard(TokenUserInfoDto tokenUserInfoDto, WikiType wikiType, Period period, Relation relation) {
        validateFilterCategory(period, relation);

        User user = findByWikiId(tokenUserInfoDto.getWikiId());
        Optional<Dashboard> dashboard = dashboardRepository.findByUserAndWikiTypeAndPeriodAndRelation(user, wikiType, period, relation);
        if (dashboard.isEmpty()) {
            return null;
        }


        if (wikiType.isNamui()) {
            Stream<DashboardType> namuiDashboardTypes = Stream.of(
                    DashboardType.BEST_WORTH,
                    DashboardType.MONEY,
                    DashboardType.HAPPY,
                    DashboardType.SAD,
                    DashboardType.CHARACTER);
            return convertToDto(namuiDashboardTypes, dashboard, period, relation);
        } else if (wikiType.isRomance()) {
            Stream<DashboardType> romanceDashboardTypes = Stream.of(
                    DashboardType.BUBBLE_CHART,
                    DashboardType.BAR_CHART,
                    DashboardType.BINARY,
                    DashboardType.RANK);
            return convertToDto(romanceDashboardTypes, dashboard, period, relation);
        } else {
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_WIKI_TYPE);
        }
    }

    private DashboardDto convertToDto(Stream<DashboardType> namuiDashboardTypes, Optional<Dashboard> dashboard, Period period, Relation relation) {
        List<Question> questions = questionRepository.findAll();

        Statistics statistics = dashboard.get().getStatistics();
        List<DashboardComponentV2> dashboardComponents = namuiDashboardTypes.flatMap(
                        dashboardType -> statistics.getStatisticsByDashboardType(dashboardType).stream()
                                .map(statistic -> {
                                    Question question = questions.stream()
                                            .filter(q -> q.getId().equals(statistic.getQuestionId()))
                                            .findFirst()
                                            .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_QUESTION_ID));
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
                                }))
                .toList();

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
