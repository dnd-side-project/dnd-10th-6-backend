package com.dnd.namuiwiki.domain.option.entity;

import com.dnd.namuiwiki.common.model.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Builder
@Document(collection = "options")
public class Option extends BaseTimeEntity {

    @Id
    private String id;
    private Object value;
    private String text;
    private String name;
    private String description;
    private int order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return Objects.equals(id, option.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
