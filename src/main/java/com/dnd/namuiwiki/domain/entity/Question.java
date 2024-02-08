package com.dnd.namuiwiki.domain.entity;

import com.dnd.namuiwiki.domain.type.QuestionType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Builder
@Document(collection = "questions")
public class Question {

    @Id
    private String id;
    private String title;
    private QuestionType type;
    @DocumentReference
    private List<Option> options;

}
