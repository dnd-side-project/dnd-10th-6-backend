package com.dnd.namuiwiki.domain.wiki;

public enum WikiType {
    NAMUI("남의위키", "다른 사람이 보는 내 모습은 어떨까요?"),
    ROMANCE("연애위키", "연애할 때 나는 어떤 사람인가요?");

    private WikiType(String title, String description) {
        this.title = title;
        this.description = description;
    }

    private final String title;
    private final String description;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
