package com.dnd.namuiwiki.domain.question.dto;

import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTypeTest {

    @Nested
    @DisplayName("문항 유형이 OX일 경우")
    class OX {
        @Test
        @DisplayName("Answer.type 'OPTION'만 허용한다.")
        void allowOnlyOptionAnswerType() {
            // given
            QuestionType questionType = QuestionType.OX;

            // when
            boolean isManual = questionType.containsAnswerType(AnswerType.MANUAL);
            boolean isOption = questionType.containsAnswerType(AnswerType.OPTION);

            // then
            assertThat(isManual).isFalse();
            assertThat(isOption).isTrue();
        }
    }

    @Nested
    @DisplayName("문항 유형이 MULTIPLE_CHOICE일 경우")
    class MULTIPLE_CHOICE {
        @Test
        @DisplayName("Answer.type 'OPTION'와 'MANUAL'을 허용한다.")
        void allowOptionAndManual() {
            // given
            QuestionType questionType = QuestionType.MULTIPLE_CHOICE;

            // when
            boolean isManual = questionType.containsAnswerType(AnswerType.MANUAL);
            boolean isOption = questionType.containsAnswerType(AnswerType.OPTION);

            // then
            assertThat(isManual).isTrue();
            assertThat(isOption).isTrue();
        }
    }

    @Nested
    @DisplayName("문항 유형이 SHORT_ANSWER일 경우")
    class SHORT_ANSWER {
        @Test
        @DisplayName("Answer.type 'MANUAL'만 허용한다.")
        void allowOnlyManualAnswerType() {
            // given
            QuestionType questionType = QuestionType.SHORT_ANSWER;

            // when
            boolean isManual = questionType.containsAnswerType(AnswerType.MANUAL);
            boolean isOption = questionType.containsAnswerType(AnswerType.OPTION);

            // then
            assertThat(isManual).isTrue();
            assertThat(isOption).isFalse();
        }
    }

    @Nested
    @DisplayName("문항 유형이 NUMBER_CHOICE일 경우")
    class NUMBER_CHOICE {
        @Test
        @DisplayName("Answer.type 'OPTION'와 'MANUAL'을 허용한다.")
        void allowOptionAndManual() {
            // given
            QuestionType questionType = QuestionType.MULTIPLE_CHOICE;

            // when
            boolean isManual = questionType.containsAnswerType(AnswerType.MANUAL);
            boolean isOption = questionType.containsAnswerType(AnswerType.OPTION);

            // then
            assertThat(isManual).isTrue();
            assertThat(isOption).isTrue();
        }
    }

}
