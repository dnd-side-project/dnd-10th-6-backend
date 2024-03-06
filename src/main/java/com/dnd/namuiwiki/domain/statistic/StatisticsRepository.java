package com.dnd.namuiwiki.domain.statistic;

import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.statistic.model.entity.PopulationStatistic;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StatisticsRepository extends MongoRepository<PopulationStatistic, String> {
    Optional<PopulationStatistic> findByPeriodAndRelationAndQuestionName(Period period, Relation relation, QuestionName name);

    List<PopulationStatistic> findAllByPeriodAndRelation(Period period, Relation relation);
}
