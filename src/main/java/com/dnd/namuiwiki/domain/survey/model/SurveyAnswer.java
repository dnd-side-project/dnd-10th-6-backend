package com.dnd.namuiwiki.domain.survey.model;

import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SurveyAnswer {
    private final List<Survey.Answer> answers;

    public List<Survey.Answer> toEntity() {
        return answers.stream().map(Survey.Answer::toEntity).toList();
    }

    public Survey.Answer get(int index) {
        return answers.get(index);
    }

    public int size() {
        return answers.size();
    }

}
