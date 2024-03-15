package com.dnd.namuiwiki.domain.statistic;

import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.statistic.model.entity.PopulationStatistic;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;

    public void updateStatistics(Survey survey) {
        Period period = survey.getPeriod();
        Relation relation = survey.getRelation();

        List<Answer> populationAnswers = survey.getAnswers().stream()
                .filter(answer -> answer.getQuestion().getDashboardType().getAnalysisType().isPopulation())
                .toList();

        populationAnswers.forEach(answer -> {
            Question question = answer.getQuestion();
            var calculationType = question.getDashboardType().getStatisticsCalculationType();
            if (calculationType.isAverage()) {
                updateAverageStatistic(question, period, relation, answer.getAnswer(Long.class));
            }
        });

        log.info("StatisticsService.updateStatistics done: period={}, relation={}, answerSize={}", period, relation, populationAnswers.size());
    }

    private void updateAverageStatistic(Question question, Period period, Relation relation, long newValue) {
        updateAverageStatisticByCategory(question, null, null, newValue);
        updateAverageStatisticByCategory(question, period, null, newValue);
        updateAverageStatisticByCategory(question, null, relation, newValue);
    }

    private void updateAverageStatisticByCategory(Question question, Period period, Relation relation, long newValue) {
        PopulationStatistic populationStatistic = getPopulationStatistic(question, period, relation);
        populationStatistic.updateStatistic(String.valueOf(newValue));

        statisticsRepository.save(populationStatistic);
    }

    private PopulationStatistic getPopulationStatistic(Question question, Period period, Relation relation) {
        return statisticsRepository.findByPeriodAndRelationAndQuestionName(period, relation, question.getName())
                .orElseGet(() -> PopulationStatistic.from(period, relation, question.getName(), question.getDashboardType()));
    }

    public List<PopulationStatistic> getPopulationStatistics(Period period, Relation relation) {
        return statisticsRepository.findAllByPeriodAndRelation(period, relation);
    }

}
