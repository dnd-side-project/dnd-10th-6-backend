package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SurveyCreatedEvent {
    private final Survey survey;
}
