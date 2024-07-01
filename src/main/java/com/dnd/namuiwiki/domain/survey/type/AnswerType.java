package com.dnd.namuiwiki.domain.survey.type;

public enum AnswerType {
    MANUAL, OPTION;

    public boolean isManual() {
        return this == MANUAL;
    }

    public boolean isOption() {
        return this == OPTION;
    }

}
