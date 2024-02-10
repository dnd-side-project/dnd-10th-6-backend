package com.dnd.namuiwiki.domain.survey.entity;

import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Builder
@Document(collection = "surveys")
public class Survey {

    @Id
    private String id;

    @DocumentReference
    private User owner;

    @DocumentReference
    private User sender;

    private String senderName;

    private Boolean isAnonymous;

    private Period period;

    private Relation relation;

    private List<Answer> answers;

    @Getter
    @Builder
    public static class Answer {

        @DocumentReference(collection = "questions", lazy = true)
        private Question question;
        private AnswerType type;
        private String answer;
        private String reason;

    }

}
