package com.dnd.namuiwiki.domain.wiki.dto;

import com.dnd.namuiwiki.domain.wiki.WikiType;
import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class WikiDto {
    private WikiType wikiType;
    private String name;
    private String description;
    private Long questionCount;
    private Long answerCount;
}
