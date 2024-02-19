package com.dnd.namuiwiki.domain.statistic;

import com.dnd.namuiwiki.domain.dashboard.DashboardRepository;
import com.dnd.namuiwiki.domain.statistic.model.Statistics;
import com.dnd.namuiwiki.domain.dashboard.model.entity.Dashboard;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final DashboardRepository dashboardRepository;

    public void updateStatistics(Survey survey) {
        User owner = survey.getOwner();
        Period period = survey.getPeriod();
        Relation relation = survey.getRelation();

        var statisticalAnswers = survey.getAnswers().stream()
                .filter(answer -> answer.getQuestion().getDashboardType().getStatisticsType().isNotNone())
                .toList();

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

}
