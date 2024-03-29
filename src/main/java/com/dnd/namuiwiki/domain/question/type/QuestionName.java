package com.dnd.namuiwiki.domain.question.type;

public enum QuestionName {
    FRIENDLINESS_LEVEL,
    PERSONALITY_TYPE,
    MBTI_IMMERSION,
    WEEKEND_COMMITMENTS,
    CORE_VALUE,
    HAPPY_BEHAVIOR,
    SAD_ANGRY_BEHAVIOR,
    BORROWING_LIMIT,
    FIRST_IMPRESSION,
    FIVE_LETTER_WORD,
    LEARNING_ASPIRATION,
    MOST_USED_WORD,
    SECRET_PLEASURE,
    CHARACTER_CELEBRITY_ASSOCIATION;

    public boolean isBorrowingLimit() {
        return this == BORROWING_LIMIT;
    }
}
