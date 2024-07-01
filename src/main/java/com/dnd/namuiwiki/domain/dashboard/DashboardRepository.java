package com.dnd.namuiwiki.domain.dashboard;

import com.dnd.namuiwiki.domain.dashboard.model.entity.Dashboard;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DashboardRepository extends MongoRepository<Dashboard, String>, DashboardCustomRepository {
    Optional<Dashboard> findByUserAndWikiTypeAndPeriodAndRelation(User user, WikiType wikiType, Period period, Relation relation);

    boolean existsByUserAndWikiTypeAndPeriodAndRelation(User user, WikiType wikiType, Period period, Relation relation);
}
