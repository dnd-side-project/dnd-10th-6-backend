package com.dnd.namuiwiki.domain.dashboard;


import com.dnd.namuiwiki.domain.dashboard.model.entity.Dashboard;
import com.dnd.namuiwiki.domain.statistic.model.Statistics;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DashboardInternalProxyService {
    private final DashboardRepository dashboardRepository;

    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 30 * 1000L)
    )
    public void updateDashboard(User owner, WikiType wikiType, Period period, Relation relation, List<Answer> answers) {
        insertDashboardIfNotExist(owner, wikiType, period, relation, answers);
        Dashboard dashboard = dashboardRepository.findByUserAndWikiTypeAndPeriodAndRelation(owner, wikiType, period, relation)
                .orElseGet(() -> {
                    Dashboard d = Dashboard.builder()
                            .user(owner)
                            .wikiType(wikiType)
                            .period(period)
                            .relation(relation)
                            .statistics(Statistics.from(answers.stream().map(Answer::getQuestion).toList()))
                            .build();
                    return dashboardRepository.save(d);
                });

        dashboard.updateStatistics(answers);
        dashboardRepository.save(dashboard);
    }

    @Recover
    private void recoverForUpdateDashboard(Exception e) {
        log.error("Failed to update dashboard", e);
    }

    private void insertDashboardIfNotExist(User owner, WikiType wikiType, Period period, Relation relation, List<Answer> answers) {
        if (!dashboardRepository.existsByUserAndWikiTypeAndPeriodAndRelation(owner, wikiType, period, relation)) {
            dashboardRepository.save(Dashboard.builder()
                    .user(owner)
                    .wikiType(wikiType)
                    .period(period)
                    .relation(relation)
                    .statistics(Statistics.from(answers.stream().map(Answer::getQuestion).toList()))
                    .build());
        }
    }

}
