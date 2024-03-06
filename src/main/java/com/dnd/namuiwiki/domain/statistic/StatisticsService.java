package com.dnd.namuiwiki.domain.statistic;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.statistic.model.AverageEntireStatistic;
import com.dnd.namuiwiki.domain.statistic.model.entity.PopulationStatistic;
import com.dnd.namuiwiki.domain.statistic.type.StatisticsCalculationType;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;

    public void updateStatistics(Survey survey) {
        Period period = survey.getPeriod();
        Relation relation = survey.getRelation();

        survey.getAnswers().stream()
                .filter(answer -> answer.getQuestion().getDashboardType().getAnalysisType().isPopulation())
                .forEach(answer -> {
                    QuestionName questionName = answer.getQuestion().getName();
                    DashboardType dashboardType = answer.getQuestion().getDashboardType();
                    StatisticsCalculationType calculationType = dashboardType.getStatisticsCalculationType();
                    if (calculationType.isAverage()) {
                        updateAverageStatistic(questionName, period, relation, answer);
                    }
                });
    }

    private void updateAverageStatistic(QuestionName questionName, Period period, Relation relation, Answer answer) {
        long newValue = answer.getAnswer(Long.class);

        updateAverageStatisticByCategory(questionName, null, null, newValue);
        updateAverageStatisticByCategory(questionName, period, null, newValue);
        updateAverageStatisticByCategory(questionName, null, relation, newValue);
    }

    private void updateAverageStatisticByCategory(QuestionName questionName, Period period, Relation relation, long newValue) {
        PopulationStatistic populationStatistic = getPopulationStatistic(period, relation, questionName);
        populationStatistic.updateStatistic(String.valueOf(newValue));

        statisticsRepository.save(populationStatistic);
    }

    private PopulationStatistic getPopulationStatistic(Period period, Relation relation, QuestionName questionName) {
        return statisticsRepository
                .findByPeriodAndRelationAndQuestionName(period, relation, questionName)
                .orElseGet(() -> PopulationStatistic.builder()
                        .statistic(new AverageEntireStatistic(0L, 0L))
                        .period(period)
                        .questionName(questionName)
                        .relation(relation)
                        .build());
    }

    public List<PopulationStatistic> getPopulationStatistics(Period period, Relation relation) {
        return statisticsRepository.findAllByPeriodAndRelation(period, relation);
    }

}
