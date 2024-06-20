package com.dnd.namuiwiki.domain.question.type;

import com.dnd.namuiwiki.domain.survey.type.AnswerType;

import java.util.List;

public enum QuestionType {
    OX(List.of(AnswerType.OPTION)),
    MULTIPLE_CHOICE(List.of(AnswerType.MANUAL, AnswerType.OPTION)),
    SHORT_ANSWER(List.of(AnswerType.MANUAL)),
    NUMERIC_CHOICE(List.of(AnswerType.MANUAL, AnswerType.OPTION)),
    RANK(List.of(AnswerType.OPTION_LIST))
    ;

    QuestionType(List<AnswerType> answerTypes) {
        this.answerTypes = answerTypes;
    }

    private final List<AnswerType> answerTypes;

    public boolean containsAnswerType(AnswerType answerType) {
        return this.answerTypes.contains(answerType);
    }

    public boolean isChoiceType() {
        return this == MULTIPLE_CHOICE || this == OX || this == NUMERIC_CHOICE;
    }

    public boolean isNumericType() {
        return this == NUMERIC_CHOICE;
    }

    public boolean isListType() {
        return this == RANK;
    }

}
