package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.domain.option.OptionRepository;
import com.dnd.namuiwiki.domain.question.QuestionRepository;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.survey.dto.AnswerDto;
import com.dnd.namuiwiki.domain.survey.model.SurveyAnswers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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
    @DisplayName("문항 유형이 OX일 경우")
    class OX {
        /**
         * 1. Answer.type이 "OPTION"만 허용한다. 아닐 경우 에러 뱉음
         * 2. Answer.answer를 통해 Option을 가져온다. Option이 없을 경우 에러 뱉음
         */

        @Test
        @DisplayName("Answer.type이 'OPTION'일 경우, Answer.answer는 Option document의 id이다.")
        void allowOnlyOptionAnswerType() {
            // given
            QuestionType questionType = QuestionType.OX;
            Question question = Question.builder().type(questionType).options(List.of()).build();
            List<AnswerDto> answers = List.of(
                    AnswerDto.builder()
                            .type("OPTION")
                            .questionId("questionId")
                            .answer("optionId")
                            .build()
            );
            given(optionRepository.existsById(any(String.class))).willReturn(true);
            given(questionRepository.findById(any(String.class))).willReturn(Optional.ofNullable(question));

            // when
            SurveyAnswers surveyAnswers = surveyAnswerService.getSurveyAnswers(answers);

            // then
            Assertions.assertThat(surveyAnswers.size()).isEqualTo(1);
        }
    }

    class MULTIPLE_CHOICE {
        /**
         * Answer.type이 "OPTION"일 경우
         * 1. Answer.answer를 통해 Option을 가져온다. Option이 없을 경우 에러 뱉음
         *
         * Answer.type이 "MANUAL"일 경우
         * 2. Answer.answer를 문자열 그대로 저장한다.
         */
    }

    class SHORT_ANSWER {
        /**
         * 1. Answer.type이 "MANUAL"만 허용한다.
         * 2. Answer.answer를 문자열 그대로 저장한다.
         */
    }

    class NUMBER_CHOICE {
        /**
         * Answer.type이 "OPTION"일 경우
         * 1. Answer.answer를 통해 Option을 가져온다. Option이 없을 경우 에러 뱉음
         *
         * Answer.type이 "MANUAL"일 경우
         * 2. Answer.answer를 숫자 그대로 저장한다.
         * 숫자가 아닐 경우 에러 뱉음
         */
    }
}