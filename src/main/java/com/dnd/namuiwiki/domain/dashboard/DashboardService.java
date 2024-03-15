package com.dnd.namuiwiki.domain.dashboard;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.dashboard.model.DashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.dto.DashboardDto;
import com.dnd.namuiwiki.domain.dashboard.model.entity.Dashboard;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.statistic.StatisticsService;
import com.dnd.namuiwiki.domain.statistic.model.entity.PopulationStatistic;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.UserRepository;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {
    private final UserRepository userRepository;
    private final DashboardRepository dashboardRepository;
    private final StatisticsService statisticsService;

    public DashboardDto getDashboard(TokenUserInfoDto tokenUserInfoDto, Period period, Relation relation) {
        validateFilterCategory(period, relation);

        User user = findByWikiId(tokenUserInfoDto.getWikiId());
        Optional<Dashboard> optionalDashboard = dashboardRepository.findByUserAndPeriodAndRelation(user, period, relation);
        if (optionalDashboard.isEmpty()) {
            return null;
        }
        Dashboard dashboard = optionalDashboard.get();

        List<PopulationStatistic> populationStatistics = statisticsService.getPopulationStatistics(period, relation);
        List<DashboardComponent> populationDashboards = dashboard.getPopulationDashboards(populationStatistics);
        List<DashboardComponent> userDashboards = dashboard.getUserDashboards();

        List<DashboardComponent> components = new ArrayList<>(userDashboards);
        components.addAll(populationDashboards);

        return new DashboardDto(components);
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
                .filter(answer -> answer.getQuestion().getDashboardType().getStatisticsCalculationType().isNotNone())
                .toList();

        updateDashboards(owner, period, relation, statisticalAnswers);

        log.info("DashboardService.updateStatistics done: owner={}, period={}, relation={}, answerSize={}", owner, period, relation, statisticalAnswers.size());
    }

    private void updateDashboards(User owner, Period period, Relation relation, List<Answer> statisticalAnswers) {
        updateDashboardByCategory(owner, null, null, statisticalAnswers);
        updateDashboardByCategory(owner, period, null, statisticalAnswers);
        updateDashboardByCategory(owner, null, relation, statisticalAnswers);
    }

    private void updateDashboardByCategory(User owner, Period period, Relation relation, List<Answer> answers) {
        Dashboard dashboard = dashboardRepository.findByUserAndPeriodAndRelation(owner, period, relation)
                .orElseGet(() -> dashboardRepository.save(Dashboard.createNew(owner, period, relation, answers)));
        dashboard.updateStatistics(answers);
        dashboardRepository.save(dashboard);
    }

}
