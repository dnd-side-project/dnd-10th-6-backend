package com.dnd.namuiwiki.domain.statistic.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.statistic.type.StatisticsType;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    private Map<String, Statistic> statistics;

    public Statistic createAndPut(Question question) {
        StatisticsType statisticsType = question.getDashboardType().getStatisticsType();
        Statistic statistic = Statistic.create(question, statisticsType);
        statistics.put(question.getId(), statistic);
        return statistic;
    }

    public Optional<Statistic> get(String questionId) {
        return Optional.ofNullable(statistics.get(questionId));
    }

    public void updateStatistics(List<Survey.Answer> answers) {
        answers.forEach(answer -> {
            Question question = answer.getQuestion();
            Statistic statistic = get(question.getId()).orElseGet(() -> createAndPut(question));

            statistic.updateStatistic(answer);
        });
    }

    public List<Statistic> getStatisticsByDashboardType(DashboardType dashboardType) {
        return statistics.values().stream()
                .filter(statistic -> statistic.getDashboardType().equals(dashboardType))
                .toList();
    }

    public static Statistics from(List<Question> questions) {
        Statistics statistics = new Statistics(new HashMap<>());
        questions.stream()
                .filter(question -> question.getDashboardType().getStatisticsType().isNotNone())
                .forEach(statistics::createAndPut);
        return statistics;
    }

}
