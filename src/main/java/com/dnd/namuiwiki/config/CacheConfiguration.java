package com.dnd.namuiwiki.config;

import com.dnd.namuiwiki.domain.question.QuestionCache;
import com.dnd.namuiwiki.domain.question.QuestionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfiguration {

    @Bean
    public QuestionCache questionCache(QuestionRepository questionRepository) {
        return new QuestionCache(questionRepository);
    }

}
