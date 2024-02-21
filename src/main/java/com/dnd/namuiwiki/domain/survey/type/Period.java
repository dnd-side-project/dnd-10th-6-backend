package com.dnd.namuiwiki.domain.survey.type;

public enum Period {

    TOTAL("전체"),
    SIX_MONTHS("6개월 미만"),
    ONE_YEAR("6개월-1년"),
    FOUR_YEARS("1년-4년"),
    INFINITE("4년 이상");

    private Period(String description) {
        this.description = description;
    }

    private final String description;

    public String getDescription() {
        return description;
    }

}
