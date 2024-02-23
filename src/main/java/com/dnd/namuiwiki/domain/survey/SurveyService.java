package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.jwt.JwtProvider;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.QuestionRepository;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.statistic.StatisticsService;
import com.dnd.namuiwiki.domain.survey.model.SurveyAnswer;
import com.dnd.namuiwiki.domain.survey.model.dto.CreateSurveyRequest;
import com.dnd.namuiwiki.domain.survey.model.dto.CreateSurveyResponse;
import com.dnd.namuiwiki.domain.survey.model.dto.GetSurveyResponse;
import com.dnd.namuiwiki.domain.survey.model.dto.GetAnswersByQuestionResponse;
import com.dnd.namuiwiki.domain.survey.model.dto.SingleAnswerWithSurveyDetailDto;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.UserRepository;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
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

    public GetSurveyResponse getSurvey(String surveyId) {
        List<Question> questions = questionRepository.findAll();
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_SURVEY));
        return GetSurveyResponse.from(survey, questions);
    }

    public GetAnswersByQuestionResponse getAnswersByQuestion(String wikiId, String questionId, Period period, Relation relation, int pageNo, int pageSize) {
        validateFilter(period, relation);

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_QUESTION_ID));

        User owner = userRepository.findByWikiId(wikiId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_USER));

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Survey> surveys = getSurveysByFilter(period, relation, owner, pageable);
        var answers = surveys.get().map(survey -> {
                    var answerOfQuestion = survey.getAnswers().stream()
                            .filter(answer -> answer.getQuestion().getId().equals(questionId))
                            .findAny()
                            .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_QUESTION_ID));
                    return SingleAnswerWithSurveyDetailDto.builder()
                            .senderName(survey.getSenderName())
                            .period(survey.getPeriod())
                            .relation(survey.getRelation())
                            .answer(convertAnswerToText(question, answerOfQuestion))
                            .reason(answerOfQuestion.getReason())
                            .build();
        }).toList();

        return new GetAnswersByQuestionResponse(question.getTitle(), answers);
    }

    private void validateFilter(Period period, Relation relation) {
        if (!period.equals(Period.TOTAL) && !relation.equals(Relation.TOTAL)) {
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_FILTER);
        }
    }

    private Page<Survey> getSurveysByFilter(Period period, Relation relation, User owner, Pageable pageable) {
        if (!period.isTotal()) {
            return surveyRepository.findByOwnerAndPeriod(owner, period, pageable);
        }
        if (!relation.isTotal()) {
            return surveyRepository.findByOwnerAndRelation(owner, relation, pageable);
        }
            return surveyRepository.findByOwner(owner, pageable);
    }

    private String convertAnswerToText(Question question, Survey.Answer answer) {
        if (answer.getType().equals(AnswerType.MANUAL)) {
            return answer.getAnswer().toString();
        }
        Option option = question.getOption(answer.getAnswer().toString())
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_OPTION_ID));
        return option.getText();
    }
}
