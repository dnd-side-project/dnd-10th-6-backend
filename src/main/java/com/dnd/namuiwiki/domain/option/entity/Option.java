package com.dnd.namuiwiki.domain.option.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "options")
public class Option {

    @Id
    private String id;
    private Object content;

}
