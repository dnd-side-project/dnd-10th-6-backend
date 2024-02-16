package com.dnd.namuiwiki.domain.statistic;

import com.dnd.namuiwiki.domain.statistic.model.entity.Dashboard;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StatisticsRepository extends MongoRepository<Dashboard, String> {
    Optional<Dashboard> findByUserAndPeriodAndRelation(User user, Period period, Relation relation);
}
