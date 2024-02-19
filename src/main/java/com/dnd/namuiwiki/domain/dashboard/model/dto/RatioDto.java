package com.dnd.namuiwiki.domain.dashboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RatioDto {
    private final String legend;
    private final int percentage;
}
