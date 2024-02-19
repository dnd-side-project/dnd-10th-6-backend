package com.dnd.namuiwiki.domain.question.entity;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Map;
import java.util.Optional;

@Getter
@Builder
@Document(collection = "questions")
public class Question {

    @Id
    private String id;
    private String title;
    private QuestionName name;
    private QuestionType type;
    private DashboardType dashboardType;
    private Long surveyOrder;
    @DocumentReference
    private Map<String, Option> options;

    public Optional<Option> getOption(String optionId) {
        return Optional.ofNullable(options.get(optionId));
    }

}
