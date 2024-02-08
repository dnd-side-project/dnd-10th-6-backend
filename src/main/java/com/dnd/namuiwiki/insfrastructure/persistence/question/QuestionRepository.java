package com.dnd.namuiwiki.insfrastructure.persistence.question;

import com.dnd.namuiwiki.domain.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, String> {
}
