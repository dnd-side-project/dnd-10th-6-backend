package com.dnd.namuiwiki.domain.question.type;

public enum QuestionName {
    /* 남의 위키 */
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
    CHARACTER_CELEBRITY_ASSOCIATION,

    /* 연애 위키 */
    INTRODUCE_BEST_FRIEND,
    DATING_TYPE,
    IDEAL_TYPE,
    ARGUMENT_BEHAVIOR,
    IMPORTANT_ELEMENT,
    FLIRTING_METHOD,
    ARGUMENT_REASON,
    ABOUT_MARRIAGE,
    ;

    public boolean isBorrowingLimit() {
        return this == BORROWING_LIMIT;
    }
}
