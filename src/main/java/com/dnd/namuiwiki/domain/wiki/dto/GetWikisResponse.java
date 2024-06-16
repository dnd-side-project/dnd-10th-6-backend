package com.dnd.namuiwiki.domain.wiki.dto;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GetWikisResponse {
    private List<WikiDto> wikiList;
}
