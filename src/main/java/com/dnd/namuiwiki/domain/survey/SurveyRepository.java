package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SurveyRepository extends MongoRepository<Survey, String> {
    Long countByOwner(User owner);
    Page<Survey> findByOwner(User owner, Pageable pageable);
}
