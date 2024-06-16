package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SurveyRepository extends MongoRepository<Survey, String> {
    Long countByOwner(User owner);
    Page<Survey> findByOwner(User owner, Pageable pageable);
    Page<Survey> findBySender(User sender, Pageable pageable);
    Page<Survey> findByOwnerAndPeriod(User owner, Period period, Pageable pageable);
    Page<Survey> findByOwnerAndRelation(User owner, Relation relation, Pageable pageable);
    Page<Survey> findBySenderAndPeriod(User sender, Period period, Pageable pageable);
    Page<Survey> findBySenderAndRelation(User sender, Relation relation, Pageable pageable);
    Long countByOwnerAndWikiType(User owner, WikiType wikiType);
}
