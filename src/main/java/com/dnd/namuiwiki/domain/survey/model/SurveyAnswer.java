package com.dnd.namuiwiki.domain.survey.model;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.question.dto.QuestionDto;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SurveyAnswer {
    private final List<Answer> answers;

    public List<Survey.Answer> toEntity() {
        return answers.stream().map(Answer::toEntity).toList();
    }

    public Answer get(int index) {
        return answers.get(index);
    }

    public int size() {
        return answers.size();
    }

    public static Answer create(QuestionDto question, AnswerType type, Object answer, String reason) {
        return new Answer(question, type, answer, reason);
    }

    @Getter
    @Builder
    public static class Answer {
        private final QuestionDto question;
        private final AnswerType type;
        private final Object answer;
        private final String reason;

        private Answer(QuestionDto question, AnswerType type, Object answer, String reason) {
            validateAnswerType(question.getType(), type);
            boolean answerShouldBeNumeric = question.getType().isNumericType() && type.isManual();
            if (answerShouldBeNumeric) {
                validateAnswerObjectType(answer, Integer.class);
            } else {
                validateAnswerObjectType(answer, String.class);
            }

            this.question = question;
            this.type = type;
            this.answer = answer;
            this.reason = reason;
        }

        private Survey.Answer toEntity() {
            return Survey.Answer.builder()
                    .question(question.toEntity())
                    .type(type)
                    .answer(answer)
                    .reason(reason)
                    .build();
        }

        private void validateAnswerObjectType(Object answer, Class<?> clazz) {
            if (!clazz.isInstance(answer)) {
                throw new ApplicationErrorException(ApplicationErrorType.NOT_INTEGER_ANSWER);
            }
        }

        private void validateAnswerType(QuestionType questionType, AnswerType answerType) {
            if (!questionType.containsAnswerType(answerType)) {
                throw new ApplicationErrorException(ApplicationErrorType.NOT_ALLOWED_ANSWER_TYPE);
            }
        }

    }
}
