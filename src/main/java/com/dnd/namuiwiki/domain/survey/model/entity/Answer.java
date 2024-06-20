package com.dnd.namuiwiki.domain.survey.model.entity;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Builder
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
            if (type.isOptionList()) {
                validateAnswerObjectType(answer, List.class);
            } else {
                validateAnswerObjectType(answer, String.class);
            }
            this.answer = answer;
        }

        this.question = question;
        this.type = type;
        this.reason = reason;
    }

    private void validateBorrowingLimit(long longAnswer) {
        if (!(0 <= longAnswer && longAnswer <= 1_000_000_000)) {
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_BORROWING_LIMIT);
        }
    }

    private void validateAnswerObjectType(Object answer, Class<?> clazz) {
        if (!clazz.isInstance(answer)) {
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_ANSWER_TYPE,
                    "예상 타입: " + clazz.getName() + ", 실제 타입: " + answer.getClass().getName());
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