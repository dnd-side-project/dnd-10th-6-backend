package com.dnd.namuiwiki.domain.survey.model.entity;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Slf4j
@Getter
@NoArgsConstructor
public class Answer {

    @DocumentReference(collection = "questions", lazy = true)
    private Question question;
    private AnswerType type;
    private Object answer;
    private String reason;

    public Answer(Question question, AnswerType type, Object answer, String reason) {
        validateAnswerType(question.getType(), type);
        validateReasonRequired(question.isReasonRequired(), reason);
        boolean shouldBeNumericAnswer = question.getType().isNumericType() && type.isManual();
        if (shouldBeNumericAnswer) {
            validateAnswerObjectType(answer, Integer.class);
            long longAnswer = Long.parseLong(answer.toString());
            if (question.getName().isBorrowingLimit()) {
                validateBorrowingLimit(longAnswer);
            }
            this.answer = longAnswer;
        } else {
            validateAnswerObjectType(answer, String.class);
            this.answer = answer;
        }

        this.question = question;
        this.type = type;
        this.reason = reason;
    }

    public <T> T getAnswer(Class<T> returnType) {
        Object value;

        if (type.isOption()) {
            Option option = question.getOptions().get(answer.toString());
            value = option.getValue();
        } else {
            value = answer;
        }

        if (value == null) {
            throw new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR);
        }

        try {
            return returnType.cast(value);
        } catch (ClassCastException e) {
            log.error("Answer.getAnswer value: {}, valueType: {}, returnType: {}", value, value.getClass(), returnType, e);
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_TYPE_CAST);
        }

    }

    private boolean isNumericAnswerNeeded() {
        return question.getType().isNumericType() && type.isManual();
    }

    private void validateBorrowingLimit(long longAnswer) {
        if (!(0 <= longAnswer && longAnswer <= 1_000_000_000)) {
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_BORROWING_LIMIT);
        }
    }

    private void validateAnswerObjectType(Object answer, Class<?> clazz) {
        if (!clazz.isInstance(answer)) {
            throw new ApplicationErrorException(ApplicationErrorType.NOT_INTEGER_ANSWER);
        }
    }

    private void validateReasonRequired(boolean reasonRequired, String reason) {
        if (reasonRequired && reason == null) {
            throw new ApplicationErrorException(ApplicationErrorType.ANSWER_REASON_REQUIRED);
        }
    }

    private void validateAnswerType(QuestionType questionType, AnswerType answerType) {
        if (!questionType.containsAnswerType(answerType)) {
            throw new ApplicationErrorException(ApplicationErrorType.NOT_ALLOWED_ANSWER_TYPE);
        }
    }

}