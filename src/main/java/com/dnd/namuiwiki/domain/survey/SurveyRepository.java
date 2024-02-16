package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SurveyRepository extends MongoRepository<Survey, String> {
}
