package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.domain.jwt.JwtProvider;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.question.QuestionRepository;
import com.dnd.namuiwiki.domain.statistic.StatisticsService;
import com.dnd.namuiwiki.domain.survey.model.SurveyAnswer;
import com.dnd.namuiwiki.domain.survey.model.dto.CreateSurveyRequest;
import com.dnd.namuiwiki.domain.user.UserRepository;
import com.dnd.namuiwiki.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SurveyServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private SurveyRepository surveyRepository;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private StatisticsService statisticsService;

    private SurveyService surveyService;

    SurveyServiceTest() {
    }

    @BeforeEach
    void beforeEach() {
        surveyService = new SurveyService(userRepository, surveyRepository, questionRepository, jwtProvider, statisticsService);
    }

    @Test
    @DisplayName("나 자신에게 설문을 작성할 수 없다.")
    void throwExceptionIfSurveyIsFromMe() {
        // given
        String ownerWikiId = "ownerWikiId";
        String senderWikiId = "senderWikiId";
        String accessToken = "accessToken";
        CreateSurveyRequest request = CreateSurveyRequest.builder().owner(ownerWikiId).build();
        SurveyAnswer surveyAnswer = new SurveyAnswer(List.of());
        User user = User.builder().wikiId(ownerWikiId).build();

        given(jwtProvider.parseToken(eq(accessToken))).willReturn(new TokenUserInfoDto(senderWikiId));
        given(userRepository.findByWikiId(eq(senderWikiId))).willReturn(Optional.ofNullable(user));
        given(userRepository.findByWikiId(eq(ownerWikiId))).willReturn(Optional.ofNullable(user));

        // when, then
        assertThatThrownBy(() -> surveyService.createSurvey(request, surveyAnswer, accessToken))
                .isInstanceOf(ApplicationErrorException.class)
                .hasMessageMatching("자신에게 설문을 보낼 수 없습니다.");
    }

}
