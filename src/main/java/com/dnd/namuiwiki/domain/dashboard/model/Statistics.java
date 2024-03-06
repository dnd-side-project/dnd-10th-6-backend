package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.statistic.type.StatisticsCalculationType;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
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
        StatisticsCalculationType statisticsCalculationType = question.getDashboardType().getStatisticsCalculationType();
        Statistic statistic = Statistic.create(question, statisticsCalculationType);
        statistics.put(question.getId(), statistic);
        return statistic;
    }

    public Optional<Statistic> get(String questionId) {
        return Optional.ofNullable(statistics.get(questionId));
    }

    public void updateStatistics(List<Answer> answers) {
        answers.forEach(answer -> {
            Question question = answer.getQuestion();
            Statistic statistic = get(question.getId()).orElseGet(() -> createAndPut(question));

            statistic.updateStatistic(answer);
        });
    }

    public Map<DashboardType, List<Statistic>> mapStatisticsByDashboardType() {
        Map<DashboardType, List<Statistic>> statistics = new HashMap<>();
        this.statistics.values().forEach(statistic -> {
            DashboardType dashboardType = statistic.getDashboardType();
            statistics.putIfAbsent(dashboardType, List.of());
            statistics.put(dashboardType, List.of(statistic));
        });

        return statistics;
    }

    public static Statistics from(List<Question> questions) {
        Statistics statistics = new Statistics(new HashMap<>());
        questions.stream()
                .filter(question -> question.getDashboardType().getStatisticsCalculationType().isNotNone())
                .forEach(statistics::createAndPut);
        return statistics;
    }

}
