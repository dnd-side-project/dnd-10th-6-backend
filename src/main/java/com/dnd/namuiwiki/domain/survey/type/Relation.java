package com.dnd.namuiwiki.domain.survey.type;

public enum Relation {

    ELEMENTARY_SCHOOL("초등학교"),
    MIDDLE_AND_HIGH_SCHOOL("중고등학교"),
    UNIVERSITY("대학교"),
    WORK("직장"),
    SOCIAL("친목 모임"),
    ETC("기타");

    private Relation(String description) {
        this.description = description;
    }

    private final String description;

    public String getDescription() {
        return description;
    }

}
