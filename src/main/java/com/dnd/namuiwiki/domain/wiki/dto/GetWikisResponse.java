package com.dnd.namuiwiki.domain.wiki.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetWikisResponse {
    private List<WikiDto> wikiList;
}
