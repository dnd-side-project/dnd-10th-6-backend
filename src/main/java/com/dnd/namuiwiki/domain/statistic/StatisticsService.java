package com.dnd.namuiwiki.domain.statistic;

import com.dnd.namuiwiki.domain.statistic.model.Statistics;
import com.dnd.namuiwiki.domain.statistic.model.entity.Dashboard;
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
    private final StatisticsRepository statisticsRepository;

    public void updateStatistics(Survey survey) {
        User owner = survey.getOwner();
        Period period = survey.getPeriod();
        Relation relation = survey.getRelation();

        var statisticalAnswers = survey.getAnswers().stream()
                .filter(answer -> answer.getQuestion().getDashboardType().getStatisticsType().isNotNone())
                .toList();

        updateStatisticsByCategory(owner, null, null, statisticalAnswers);
        updateStatisticsByCategory(owner, period, null, statisticalAnswers);
        updateStatisticsByCategory(owner, null, relation, statisticalAnswers);
    }

    private void updateStatisticsByCategory(User owner, Period period, Relation relation, List<Survey.Answer> answers) {
        Dashboard dashboard = statisticsRepository.findByUserAndPeriodAndRelation(owner, period, relation)
                .orElseGet(() -> {
                    Dashboard newDashboard = Dashboard.builder()
                            .user(owner)
                            .period(period)
                            .relation(relation)
                            .statistics(Statistics.from(answers.stream().map(Survey.Answer::getQuestion).toList()))
                            .build();
                    return statisticsRepository.save(newDashboard);
                });
        dashboard.updateStatistics(answers);
        statisticsRepository.save(dashboard);
    }

}
