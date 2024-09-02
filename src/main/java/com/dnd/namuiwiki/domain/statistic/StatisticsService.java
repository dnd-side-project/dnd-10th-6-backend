package com.dnd.namuiwiki.domain.statistic;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.statistic.model.BorrowingLimitEntireStatistic;
import com.dnd.namuiwiki.domain.statistic.model.entity.PopulationStatistic;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;

    public void updateStatistics(Survey survey) {
        WikiType wikiType = survey.getWikiType();
        Period period = survey.getPeriod();
        Relation relation = survey.getRelation();

        var answers = survey.getAnswers().stream()
                .filter(answer -> answer.getQuestion().getDashboardType().getStatisticsType().isNotNone())
                .toList();

        if (wikiType.isNamui()) {
            updateBorrowingLimitStatistic(period, relation, answers);
        }
    }

    public PopulationStatistic getPopulationStatistic(Period period, Relation relation, QuestionName questionName) {
        return statisticsRepository
                .findByPeriodAndRelationAndQuestionName(period, relation, questionName)
                .orElseGet(() -> PopulationStatistic.builder()
                        .statistic(new BorrowingLimitEntireStatistic(0L, 0L))
                        .period(period)
                        .questionName(questionName)
                        .relation(relation)
                        .build());
    }

    private void updateBorrowingLimitStatistic(Period period, Relation relation, List<Answer> answers) {
        Answer borroingLimitAnswer = answers.stream()
                .filter(answer -> answer.getQuestion().getName() == QuestionName.BORROWING_LIMIT)
                .findFirst()
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_DATA_ARGUMENT));

        long borrowingLimit;
        if (borroingLimitAnswer.getType().isOption()) {
            Option option = borroingLimitAnswer.getQuestion().getOptions().get(borroingLimitAnswer.getAnswer().toString());
            borrowingLimit = (long) option.getValue();
        } else {
            borrowingLimit = (long) borroingLimitAnswer.getAnswer();
        }

        updateBorrowingLimitStatisticByCategory(null, null, borrowingLimit);
        updateBorrowingLimitStatisticByCategory(period, null, borrowingLimit);
        updateBorrowingLimitStatisticByCategory(null, relation, borrowingLimit);
    }

    private void updateBorrowingLimitStatisticByCategory(Period period, Relation relation, long borrowingLimit) {
        PopulationStatistic populationStatistic = getPopulationStatistic(period, relation, QuestionName.BORROWING_LIMIT);

        BorrowingLimitEntireStatistic statistic = (BorrowingLimitEntireStatistic) populationStatistic.getStatistic();
        statistic.updateStatistic(String.valueOf(borrowingLimit));

        populationStatistic.setStatistic(statistic);

        statisticsRepository.save(populationStatistic);
    }

}
