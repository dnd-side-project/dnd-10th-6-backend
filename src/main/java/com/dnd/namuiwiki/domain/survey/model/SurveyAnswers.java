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
public class SurveyAnswers {
    private final List<SurveyAnswer> answers;

    public List<Survey.Answer> toEntity() {
        return answers.stream().map(SurveyAnswer::toEntity).toList();
    }

    public int size() {
        return answers.size();
    }

    public static SurveyAnswer createSurveyAnswer(QuestionDto question, AnswerType type, String answer, String reason) {
        return new SurveyAnswer(question, type, answer, reason);
    }

    @Getter
    @Builder
    public static class SurveyAnswer {
        private final QuestionDto question;
        private final AnswerType type;
        private final String answer;
        private final String reason;

        private SurveyAnswer(QuestionDto question, AnswerType type, String answer, String reason) {
            validateAnswerType(question.getType(), type);

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

        private void validateAnswerType(QuestionType questionType, AnswerType answerType) {
            if (!questionType.containsAnswerType(answerType)) {
                throw new ApplicationErrorException(ApplicationErrorType.NOT_ALLOWED_ANSWER_TYPE);
            }
        }

    }
}
