package com.dnd.namuiwiki.domain.survey.model.entity;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.common.model.BaseTimeEntity;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
@Document(collection = "surveys")
public class Survey extends BaseTimeEntity {

    @Id
    private String id;

    @DocumentReference
    private User owner;

    @DocumentReference
    private User sender;

    private String senderName;

    private Period period;

    private Relation relation;

    private List<Answer> answers;

    public Survey.Answer getAnswer(int index) {
        return answers.get(index);
    }

    public int size() {
        return answers.size();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    public static class Answer {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Survey survey = (Survey) o;
        return Objects.equals(id, survey.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public LocalDateTime getWrittenAt() {
        return this.createdAt;
    }
}
