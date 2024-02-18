package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.domain.option.OptionRepository;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.QuestionRepository;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.survey.model.SurveyAnswer;
import com.dnd.namuiwiki.domain.survey.model.dto.AnswerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SurveyAnswerServiceTest {
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private OptionRepository optionRepository;

    private SurveyAnswerService surveyAnswerService;

    @BeforeEach
    void beforeEach() {
        surveyAnswerService = new SurveyAnswerService(questionRepository, optionRepository);
    }

    @Nested
    @DisplayName("Answer.type이 'OPTION'일 경우")
    class AnswerTypeIsOption {
        private final AnswerDto answerOfOptionType = AnswerDto.builder()
                .type("OPTION")
                .questionId("questionId")
                .answer("optionId")
                .build();

        @Test
        @DisplayName("Answer.answer는 Option document의 id이다.")
        void answerIsDocumentId() {
            // given
            QuestionType questionType = QuestionType.OX;
            Option option = Option.builder().id("optionId").build();
            Question question = Question.builder().type(questionType).options(Map.of(option.getId(), option)).build();
            var answers = List.of(answerOfOptionType);

            given(optionRepository.existsById(any(String.class))).willReturn(true);
            given(questionRepository.findById(any(String.class))).willReturn(Optional.of(question));

            // when
            SurveyAnswer surveyAnswer = surveyAnswerService.getSurveyAnswers(answers);

            // then
            assertThat(surveyAnswer.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("Answer.answer에 해당하는 Option이 없을 경우 에러 발생")
        void throwExceptionIfOptionDocuementNotExists() {
            // given
            QuestionType questionType = QuestionType.OX;
            Question question = Question.builder().type(questionType).options(Map.of()).build();
            var answers = List.of(answerOfOptionType);

            given(optionRepository.existsById(any(String.class))).willReturn(false);
            given(questionRepository.findById(any(String.class))).willReturn(Optional.of(question));

            // then
            assertThatThrownBy(() -> surveyAnswerService.getSurveyAnswers(answers))
                    .isInstanceOf(ApplicationErrorException.class)
                    .hasMessageMatching("옵션을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("questionId에 해당하는 Question이 없을 경우 에러 발생")
        void throwExceptionIfQuestionDocuementNotExists() {
            // given
            var answers = List.of(answerOfOptionType);

            given(questionRepository.findById(any(String.class))).willReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> surveyAnswerService.getSurveyAnswers(answers))
                    .isInstanceOf(ApplicationErrorException.class)
                    .hasMessageMatching("문항을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("Option이 해당 Question의 선지가 아닐 경우 에러 발생")
        void throwExceptionIfQuestionDoesntHaveOption() {
            // given
            QuestionType questionType = QuestionType.OX;
            Option option = Option.builder().id("notOptionId").build();
            Option realOption = Option.builder().id("realOptionId").build();
            Question question = Question.builder().type(questionType).options(Map.of(realOption.getId(), realOption)).build();
            var answers = List.of(answerOfOptionType);

            given(optionRepository.existsById(any(String.class))).willReturn(true);
            given(questionRepository.findById(any(String.class))).willReturn(Optional.of(question));

            // then
            assertThatThrownBy(() -> surveyAnswerService.getSurveyAnswers(answers))
                    .isInstanceOf(ApplicationErrorException.class)
                    .hasMessageMatching("문항에 해당 옵션이 없습니다.");
        }

    }

    @Nested
    @DisplayName("Answer.type이 'MANUAL'일 경우")
    class AnswerTypeIsManual {
        private final AnswerDto answerOfManualType = AnswerDto.builder()
                .type("MANUAL")
                .questionId("questionId")
                .answer("직접 작성한 답변")
                .build();

        @Test
        @DisplayName("Answer.answer는 일반 문자열로 취급한다.")
        void answerIsPlainText() {
            // given
            QuestionType questionType = QuestionType.SHORT_ANSWER;
            Question question = Question.builder().type(questionType).options(Map.of()).build();
            var answers = List.of(answerOfManualType);

            given(questionRepository.findById(any(String.class))).willReturn(Optional.of(question));

            // when
            var answer = surveyAnswerService.getSurveyAnswers(answers).get(0);

            // then
            String expected = "직접 작성한 답변";
            assertThat(answer.getAnswer()).isEqualTo(expected);
        }

        @Test
        @DisplayName("MANUAL 타입의 Question은 Option 리스트의 길이가 0이다.")
        void sizeOfOptionListIsZero() {
            // given
            QuestionType questionType = QuestionType.SHORT_ANSWER;
            Question question = Question.builder().type(questionType).options(Map.of()).build();
            var answers = List.of(answerOfManualType);

            given(questionRepository.findById(any(String.class))).willReturn(Optional.of(question));

            // when
            var answer = surveyAnswerService.getSurveyAnswers(answers).get(0);

            // then
            int expected = 0;
            assertThat(answer.getQuestion().getOptions().size()).isEqualTo(expected);
        }

    }

    @Nested
    @DisplayName("Question 타입이 NUMBER_CHOICE일 경우")
    class NUMBER_CHOICE {

        @Test
        @DisplayName("답변이 MANUAL타입이면, answer는 Long 타입이다.")
        void typeOfAnswerShouldBeInteger() {
            // given
            QuestionType questionType = QuestionType.NUMERIC_CHOICE;
            Question question = Question.builder().type(questionType).options(Map.of()).build();
            int intAnswer = 100000;
            var answers = List.of(AnswerDto.builder()
                    .type("MANUAL")
                    .questionId("questionId")
                    .answer(intAnswer)
                    .build());

            given(questionRepository.findById(any(String.class))).willReturn(Optional.of(question));

            // when
            var answer = surveyAnswerService.getSurveyAnswers(answers).get(0);

            // then
            assertThat(answer.getAnswer() instanceof Long).isTrue();
        }

        @Test
        @DisplayName("답변이 MANUAL타입일 때 answer가 숫자가 아니면 에러 발생")
        void throwExceptionIfAnswerIsNotInteger() {
            // given
            QuestionType questionType = QuestionType.NUMERIC_CHOICE;
            Question question = Question.builder().type(questionType).options(Map.of()).build();
            String stringAnswer = "100000";
            var answers = List.of(AnswerDto.builder()
                    .type("MANUAL")
                    .questionId("questionId")
                    .answer(stringAnswer)
                    .build());

            given(questionRepository.findById(any(String.class))).willReturn(Optional.of(question));

            // then
            assertThatThrownBy(() -> surveyAnswerService.getSurveyAnswers(answers))
                    .isInstanceOf(ApplicationErrorException.class)
                    .hasMessageMatching("정수형 답변이 아닙니다.");
        }

    }
}
