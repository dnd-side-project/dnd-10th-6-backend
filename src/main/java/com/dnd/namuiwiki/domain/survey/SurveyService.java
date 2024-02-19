package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.jwt.JwtProvider;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.statistic.StatisticsService;
import com.dnd.namuiwiki.domain.survey.model.SurveyAnswer;
import com.dnd.namuiwiki.domain.survey.model.dto.CreateSurveyRequest;
import com.dnd.namuiwiki.domain.survey.model.dto.CreateSurveyResponse;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.UserRepository;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final JwtProvider jwtProvider;
    private final StatisticsService statisticsService;

    public CreateSurveyResponse createSurvey(CreateSurveyRequest request, SurveyAnswer surveyAnswer, String accessToken) {
        User owner = userRepository.findByWikiId(request.getOwner())
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_USER));
        User sender = getUserByAccessToken(accessToken);

//        validateNotFromMe(owner, sender);

        Survey survey = surveyRepository.save(Survey.builder()
                .owner(owner)
                .sender(sender)
                .senderName(request.getSenderName())
                .isAnonymous(request.getIsAnonymous())
                .period(Period.valueOf(request.getPeriod()))
                .relation(Relation.valueOf(request.getRelation()))
                .answers(surveyAnswer.toEntity())
                .build());

        statisticsService.updateStatistics(survey);

        return new CreateSurveyResponse(survey.getId());
    }

    private void validateNotFromMe(User owner, User sender) {
        if (owner.equals(sender)) {
            throw new ApplicationErrorException(ApplicationErrorType.CANNOT_SEND_SURVEY_TO_MYSELF);
        }
    }

    private User getUserByAccessToken(String accessToken) {
        if (accessToken == null) {
            return null;
        }

        TokenUserInfoDto tokenUserInfoDto = jwtProvider.parseToken(accessToken);
        return userRepository.findByWikiId(tokenUserInfoDto.getWikiId())
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_USER));
    }

}
