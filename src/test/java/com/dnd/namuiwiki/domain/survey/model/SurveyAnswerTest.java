package com.dnd.namuiwiki.domain.survey.model;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.domain.question.dto.QuestionDto;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
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
        SurveyAnswer surveyAnswer = new SurveyAnswer(List.of(
                new Survey.Answer(QuestionDto.builder().id("questionId").options(List.of()).type(QuestionType.OX).build(), AnswerType.OPTION, "O", null),
                new Survey.Answer(QuestionDto.builder().id("questionId").options(List.of()).type(QuestionType.SHORT_ANSWER).build(), AnswerType.MANUAL, "답변", null)
        ));

        // when
        int size = surveyAnswer.size();

        // then
        assertThat(size).isEqualTo(2);
    }

    @Test
    @DisplayName("OX 문항에서 answer.type이 'OPTION'이 아닐 경우 에러가 발생한다.")
    void throwExceptionIfAnswerTypeIsNotOption() {
        // given
        QuestionDto question = QuestionDto.builder().type(QuestionType.OX).build();
        AnswerType answerType = AnswerType.MANUAL;

        // then
        assertThatThrownBy(() -> new Survey.Answer(question, answerType, null, null))
                .isInstanceOf(ApplicationErrorException.class);
    }

    @Test
    @DisplayName("SHORT_ANSWER 문항에서 answer.type이 'MANUAL'이 아닐 경우 에러가 발생한다.")
    void throwExceptionIfAnswerTypeIsNotManual() {
        // given
        QuestionDto question = QuestionDto.builder().type(QuestionType.SHORT_ANSWER).build();
        AnswerType answerType = AnswerType.OPTION;

        // then
        assertThatThrownBy(() -> new Survey.Answer(question, answerType, null, null))
                .isInstanceOf(ApplicationErrorException.class);
    }

}
