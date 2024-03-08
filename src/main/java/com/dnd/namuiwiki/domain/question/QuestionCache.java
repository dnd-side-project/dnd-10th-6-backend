package com.dnd.namuiwiki.domain.question;

import com.dnd.namuiwiki.domain.question.entity.Question;
import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class QuestionCache {

    private final QuestionRepository questionRepository;

    private Map<String, Question> cachedQuestions;

    public QuestionCache(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @PostConstruct
    public void initializeCache() {
        Map<String, Question> objectObjectHashMap = new HashMap<>();
        questionRepository.findAll()
                .forEach(question -> objectObjectHashMap.put(question.getId(), question));
        this.cachedQuestions = objectObjectHashMap;
    }

    public Map<String, Question> findAll() {
        return cachedQuestions;
    }

    public Optional<Question> findById(String id) {
        return Optional.ofNullable(cachedQuestions.get(id));
    }

}
