package com.dnd.namuiwiki.domain.survey.model.entity;

import com.dnd.namuiwiki.common.model.BaseTimeEntity;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
@Document(collection = "surveys")
public class Survey extends BaseTimeEntity {

    @Id
    private String id;

    @DocumentReference(collection = "users", lazy = true)
    private User owner;

    @DocumentReference(collection = "users", lazy = true)
    private User sender;

    private String senderName;

    private Period period;

    private Relation relation;

    private List<Answer> answers;

    private WikiType wikiType;

    public Answer getAnswer(int index) {
        return answers.get(index);
    }

    public int size() {
        return answers.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Survey survey = (Survey) o;
        return Objects.equals(id, survey.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public LocalDateTime getWrittenAt() {
        return this.createdAt;
    }
}
