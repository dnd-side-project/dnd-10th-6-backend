package com.dnd.namuiwiki.domain.question.type;

import com.dnd.namuiwiki.domain.survey.type.AnswerType;

import java.util.List;

public enum QuestionType {
    OX(List.of(AnswerType.OPTION)),
    MULTIPLE_CHOICE(List.of(AnswerType.MANUAL, AnswerType.OPTION)),
    SHORT_ANSWER(List.of(AnswerType.MANUAL)),
    NUMBER_CHOICE(List.of(AnswerType.MANUAL, AnswerType.OPTION));

    QuestionType(List<AnswerType> answerTypes) {
        this.answerTypes = answerTypes;
    }

    private final List<AnswerType> answerTypes;

    public boolean containsAnswerType(AnswerType answerType) {
        return this.answerTypes.contains(answerType);
    }

    public boolean isChoiceType() {
        return this == MULTIPLE_CHOICE || this == OX || this == NUMBER_CHOICE;
    }

    public boolean isNumericType() {
        return this == NUMBER_CHOICE;
    }

}
