package com.dnd.namuiwiki.domain.question.type;

public enum QuestionType {
    OX, MULTIPLE_CHOICE, SHORT_ANSWER, NUMBER_CHOICE;

    public boolean isChoice() {
        return this == MULTIPLE_CHOICE || this == OX || this == NUMBER_CHOICE;
    }

    public static QuestionType of(String type) {
        return QuestionType.valueOf(type);
    }
}
