package com.dnd.namuiwiki.application.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionJsonDto {
    private String title;
    private String type;
    private Long[] options;
}
