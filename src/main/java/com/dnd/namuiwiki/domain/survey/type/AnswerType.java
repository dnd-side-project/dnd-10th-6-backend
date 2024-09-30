package com.dnd.namuiwiki.domain.survey.type;

public enum AnswerType {
    MANUAL, OPTION, OPTION_LIST;

    public boolean isManual() {
        return this == MANUAL;
    }

    public boolean isOption() {
        return this == OPTION;
    }

    public boolean isOptionList() {
        return this == OPTION_LIST;
    }

}
