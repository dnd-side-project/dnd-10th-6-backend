package com.dnd.namuiwiki.domain.question;

import com.dnd.namuiwiki.domain.question.entity.Question;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends MongoRepository<Question, String> {

    @NonNull
    @Cacheable("questions")
    List<Question> findAll();

    @NonNull
    @Cacheable("question")
    Optional<Question> findById(@NonNull String id);

}
