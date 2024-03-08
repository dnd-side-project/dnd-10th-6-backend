package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.domain.dashboard.DashboardService;
import com.dnd.namuiwiki.domain.jwt.JwtProvider;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.QuestionCache;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.statistic.StatisticsService;
import com.dnd.namuiwiki.domain.survey.model.dto.AnswerDto;
import com.dnd.namuiwiki.domain.survey.model.dto.CreateSurveyRequest;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.user.UserRepository;
import com.dnd.namuiwiki.domain.user.entity.User;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SurveyServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private SurveyRepository surveyRepository;
    @Mock
    private QuestionCache questionCache;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private StatisticsService statisticsService;
    @Mock
    private DashboardService dashboardService;

    private SurveyService surveyService;

    @BeforeEach
    void beforeEach() {
        surveyService = new SurveyService(userRepository, surveyRepository, questionCache, jwtProvider, statisticsService, dashboardService);
    }

    @Test
    @DisplayName("나 자신에게 설문을 작성할 수 없다.")
    void throwExceptionIfSurveyIsFromMe() {
        // given
        String ownerWikiId = "ownerWikiId";
        String senderWikiId = "senderWikiId";
        String accessToken = "accessToken";
        CreateSurveyRequest request = CreateSurveyRequest.builder().answers(List.of()).owner(ownerWikiId).build();
        User user = User.builder().wikiId(ownerWikiId).build();

        given(jwtProvider.parseToken(eq(accessToken))).willReturn(new TokenUserInfoDto(senderWikiId));
        given(userRepository.findByWikiId(eq(senderWikiId))).willReturn(Optional.ofNullable(user));
        given(userRepository.findByWikiId(eq(ownerWikiId))).willReturn(Optional.ofNullable(user));

        // when, then
        assertThatThrownBy(() -> surveyService.createSurvey(request, accessToken))
                .isInstanceOf(ApplicationErrorException.class)
                .hasMessageMatching("자신에게 설문을 보낼 수 없습니다.");
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
            Question question = Question.builder().id("questionId").type(questionType).options(Map.of(option.getId(), option)).build();

            given(questionCache.findById(any(String.class))).willReturn(Optional.of(question));

            // when
            Answer surveyAnswer = surveyService.convertAnswer(answerOfOptionType);

            // then
            assertThat(surveyAnswer.getAnswer()).isEqualTo(option.getId());
        }

        @Test
        @DisplayName("Answer.answer에 해당하는 Option이 없을 경우 에러 발생")
        void throwExceptionIfOptionDocuementNotExists() {
            // given
            QuestionType questionType = QuestionType.OX;
            Question question = Question.builder().id("questionId").type(questionType).options(Map.of()).build();

            given(questionCache.findById(any(String.class))).willReturn(Optional.of(question));

            // then
            assertThatThrownBy(() -> surveyService.convertAnswer(answerOfOptionType))
                    .isInstanceOf(ApplicationErrorException.class)
                    .hasMessageMatching("옵션을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("questionId에 해당하는 Question이 없을 경우 에러 발생")
        void throwExceptionIfQuestionDocuementNotExists() {
            // given
            given(questionCache.findById(any(String.class))).willReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> surveyService.convertAnswer(answerOfOptionType))
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
            Question question = Question.builder().id("questionId").type(questionType).options(Map.of(realOption.getId(), realOption)).build();
            var answers = List.of(answerOfOptionType);

            given(questionCache.findById(any(String.class))).willReturn(Optional.of(question));

            // then
            assertThatThrownBy(() -> surveyService.convertAnswer(answerOfOptionType))
                    .isInstanceOf(ApplicationErrorException.class)
                    .hasMessageMatching("옵션을 찾을 수 없습니다.");
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
            Question question = Question.builder().id("questionId").type(questionType).options(Map.of()).build();
            var answers = List.of(answerOfManualType);

            given(questionCache.findById(any(String.class))).willReturn(Optional.of(question));

            // when
            Answer answer = surveyService.convertAnswer(answerOfManualType);

            // then
            String expected = "직접 작성한 답변";
            assertThat(answer.getAnswer()).isEqualTo(expected);
        }

        @Test
        @DisplayName("MANUAL 타입의 Question은 Option 리스트의 길이가 0이다.")
        void sizeOfOptionListIsZero() {
            // given
            QuestionType questionType = QuestionType.SHORT_ANSWER;
            Question question = Question.builder().id("questionId").type(questionType).options(Map.of()).build();
            var answers = List.of(answerOfManualType);

            given(questionCache.findById(any(String.class))).willReturn(Optional.of(question));

            // when
            Answer answer = surveyService.convertAnswer(answerOfManualType);

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
            Question question = Question.builder().id("questionId").type(questionType).options(Map.of()).name(QuestionName.BORROWING_LIMIT).build();
            int intAnswer = 100000;

            given(questionCache.findById(any(String.class))).willReturn(Optional.of(question));

            // when
            Answer answer = surveyService.convertAnswer(AnswerDto.builder()
                    .type("MANUAL")
                    .questionId("questionId")
                    .answer(intAnswer)
                    .build());

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
            var answer = AnswerDto.builder()
                    .type("MANUAL")
                    .questionId("questionId")
                    .answer(stringAnswer)
                    .build();

            given(questionCache.findById(any(String.class))).willReturn(Optional.of(question));

            // then
            assertThatThrownBy(() -> surveyService.convertAnswer(answer))
                    .isInstanceOf(ApplicationErrorException.class)
                    .hasMessageMatching("정수형 답변이 아닙니다.");
        }

    }

}
