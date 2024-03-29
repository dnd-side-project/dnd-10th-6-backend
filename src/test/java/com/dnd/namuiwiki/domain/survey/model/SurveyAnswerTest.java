package com.dnd.namuiwiki.domain.survey.model;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SurveyAnswerTest {

    @Test
    @DisplayName("SurveyAnswer의 size 메서드 테스트")
    void size() {
        // given
        Survey survey = Survey.builder().answers(List.of(
                        new Answer(Question.builder().id("questionId").type(QuestionType.OX).build(), AnswerType.OPTION, "O", null),
                        new Answer(Question.builder().id("questionId").type(QuestionType.SHORT_ANSWER).build(), AnswerType.MANUAL, "답변", null)
                ))
                .build();

        // when
        int size = survey.size();

        // then
        assertThat(size).isEqualTo(2);
    }

    @Test
    @DisplayName("OX 문항에서 answer.type이 'OPTION'이 아닐 경우 에러가 발생한다.")
    void throwExceptionIfAnswerTypeIsNotOption() {
        // given
        Question question = Question.builder().type(QuestionType.OX).build();
        AnswerType answerType = AnswerType.MANUAL;

        // then
        assertThatThrownBy(() -> new Answer(question, answerType, null, null))
                .isInstanceOf(ApplicationErrorException.class);
    }

    @Test
    @DisplayName("SHORT_ANSWER 문항에서 answer.type이 'MANUAL'이 아닐 경우 에러가 발생한다.")
    void throwExceptionIfAnswerTypeIsNotManual() {
        // given
        Question question = Question.builder().type(QuestionType.SHORT_ANSWER).build();
        AnswerType answerType = AnswerType.OPTION;

        // then
        assertThatThrownBy(() -> new Answer(question, answerType, null, null))
                .isInstanceOf(ApplicationErrorException.class);
    }

}
