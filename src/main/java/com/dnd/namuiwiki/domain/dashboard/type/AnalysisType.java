package com.dnd.namuiwiki.domain.dashboard.type;

public enum AnalysisType {
    USER, POPULATION;

    public boolean isUser() {
        return this == USER;
    }

    public boolean isPopulation() {
        return this == POPULATION;
    }
}
