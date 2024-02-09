package com.dnd.namuiwiki.domain.question;

import com.dnd.namuiwiki.domain.question.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, String> {
}
