package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.dto.PageableDto;
import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.common.util.ListUtils;
import com.dnd.namuiwiki.domain.jwt.JwtService;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.option.OptionRepository;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.QuestionRepository;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.survey.model.dto.AnswerDto;
import com.dnd.namuiwiki.domain.survey.model.dto.CreateSurveyRequest;
import com.dnd.namuiwiki.domain.survey.model.dto.CreateSurveyResponse;
import com.dnd.namuiwiki.domain.survey.model.dto.GetAnswersByQuestionResponse;
import com.dnd.namuiwiki.domain.survey.model.dto.GetSurveyResponse;
import com.dnd.namuiwiki.domain.survey.model.dto.ReceivedSurveyDto;
import com.dnd.namuiwiki.domain.survey.model.dto.SentSurveyDto;
import com.dnd.namuiwiki.domain.survey.model.dto.SingleAnswerWithSurveyDetailDto;
import com.dnd.namuiwiki.domain.survey.model.dto.SurveyCreatedEvent;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.UserRepository;
import com.dnd.namuiwiki.domain.user.entity.User;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyService {
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final JwtService jwtService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CreateSurveyResponse createSurvey(CreateSurveyRequest request, String accessToken) {
        User owner = getUserByWikiId(request.getOwner());
        User sender = jwtService.getUserByAccessToken(accessToken);
        validateNotFromMe(owner, sender);

        List<Answer> surveyAnswer = request.getAnswers().stream().map(this::convertAnswer).toList();
        validateQuestionWikiType(surveyAnswer, request.getWikiType());

        Survey survey = surveyRepository.save(Survey.builder()
                .owner(owner)
                .wikiType(WikiType.valueOf(request.getWikiType()))
                .sender(sender)
                .senderName(request.getSenderName())
                .period(Period.valueOf(request.getPeriod()))
                .relation(Relation.valueOf(request.getRelation()))
                .answers(surveyAnswer)
                .build());

        log.info("SurveyService.createSurvey: surveyId={} done", survey.getId());

        applicationEventPublisher.publishEvent(new SurveyCreatedEvent(survey));

        return new CreateSurveyResponse(survey.getId());
    }

    private void validateQuestionWikiType(List<Answer> surveyAnswer, String wikiType) {
        List<Question> questions = questionRepository.findAll();
        surveyAnswer.forEach(answer -> {
            Question question = questions.stream()
                    .filter(q -> q.getId().equals(answer.getQuestion().getId()))
                    .findAny()
                    .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_QUESTION_ID));
            if (!question.getWikiType().equals(WikiType.valueOf(wikiType))) {
                throw new ApplicationErrorException(ApplicationErrorType.INVALID_QUESTION_WIKI_TYPE);
            }
        });
    }

    public Answer convertAnswer(AnswerDto answer) {
        Question question = getQuestionById(answer.getQuestionId());
        AnswerType answerType = AnswerType.valueOf(answer.getType());
        var surveyAnswer = new Answer(question, answerType, answer.getAnswer(), answer.getReason());

        if (answerType.isOptionList()) {
            List<String> optionIds = (List<String>) answer.getAnswer();
            optionIds.forEach(optionId -> validateOptionExists(optionId, question));
        }

        if (answerType.isOption()) {
            String optionId = answer.getAnswer().toString();
            validateOptionExists(optionId, question);
        }

        return surveyAnswer;
    }

    private void validateNotFromMe(User owner, User sender) {
        if (owner.isMe(sender)) {
            throw new ApplicationErrorException(ApplicationErrorType.CANNOT_SEND_SURVEY_TO_MYSELF);
        }
    }

    private void validateOptionExists(String optionId, Question question) {
        if (!optionRepository.existsById(optionId)) {
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_OPTION_ID);
        }
        if (!question.getOptions().containsKey(optionId)) {
            throw new ApplicationErrorException(ApplicationErrorType.CONFLICT_OPTION_QUESTION);
        }
    }

    public PageableDto<ReceivedSurveyDto> getReceivedSurveys(
            TokenUserInfoDto tokenUserInfoDto, WikiType wikiType,
            int pageNo, int pageSize
    ) {
        User user = getUserByWikiId(tokenUserInfoDto.getWikiId());

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<ReceivedSurveyDto> surveys = surveyRepository.findByOwnerAndWikiType(user, wikiType, pageable)
                .map(ReceivedSurveyDto::from);
        return PageableDto.create(surveys);
    }

    public PageableDto<SentSurveyDto> getSentSurveys(
            TokenUserInfoDto tokenUserInfoDto, Period period, Relation relation,
            int pageNo, int pageSize
    ) {
        validateFilter(period, relation);
        User sender = getUserByWikiId(tokenUserInfoDto.getWikiId());

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<SentSurveyDto> surveys = getSentSurveysByFilter(period, relation, sender, pageable)
                .map(SentSurveyDto::from);
        return PageableDto.create(surveys);
    }

    public GetSurveyResponse getSurvey(String surveyId, TokenUserInfoDto tokenUserInfoDto) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_SURVEY));

        User user = getUserByWikiId(tokenUserInfoDto.getWikiId());
        validateSurveyOwner(survey, user);

        List<Question> questions = questionRepository.findAll();
        return GetSurveyResponse.from(survey, questions);
    }

    private void validateSurveyOwner(Survey survey, User user) {
        if (!survey.getOwner().isMe(user)) {
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_SURVEY_OWNER);
        }
    }

    public GetAnswersByQuestionResponse getAnswersByQuestion(
            WikiType wikiType,
            String wikiId, String questionId,
            Period period, Relation relation,
            int pageNo, int pageSize
    ) {
        validateFilter(period, relation);

        Question question = getQuestionById(questionId);
        User owner = getUserByWikiId(wikiId);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Survey> surveys = getReceivedSurveysByFilter(wikiType, period, relation, owner, pageable);
        var answers = surveys.map(survey -> survey.getAnswers().stream()
                .filter(answer -> answer.getQuestion().getId().equals(questionId))
                .findAny()
                .map(answer -> SingleAnswerWithSurveyDetailDto.builder()
                        .senderName(survey.getSenderName())
                        .period(survey.getPeriod())
                        .relation(survey.getRelation())
                        .createdAt(survey.getWrittenAt())
                        .answer(convertAnswer(question, answer))
                        .reason(answer.getReason())
                        .optionName(question, answer)
                        .wikiType(survey.getWikiType())
                        .build()).orElse(null));

        return new GetAnswersByQuestionResponse(question.getTitle(), question.getName(), PageableDto.create(answers));
    }

    private void validateFilter(Period period, Relation relation) {
        if (!period.equals(Period.TOTAL) && !relation.equals(Relation.TOTAL)) {
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_FILTER);
        }
    }

    private Page<Survey> getSentSurveysByFilter(Period period, Relation relation, User sender, Pageable pageable) {
        if (!period.isTotal()) {
            return surveyRepository.findBySenderAndPeriod(sender, period, pageable);
        }
        if (!relation.isTotal()) {
            return surveyRepository.findBySenderAndRelation(sender, relation, pageable);
        }
        return surveyRepository.findBySender(sender, pageable);
    }

    private Page<Survey> getReceivedSurveysByFilter(WikiType wikiType, Period period, Relation relation, User owner, Pageable pageable) {
        if (!period.isTotal()) {
            return surveyRepository.findByWikiTypeAndOwnerAndPeriod(wikiType, owner, period, pageable);
        }
        if (!relation.isTotal()) {
            return surveyRepository.findByWikiTypeAndOwnerAndRelation(wikiType, owner, relation, pageable);
        }
        return surveyRepository.findByWikiTypeAndOwner(wikiType, owner, pageable);
    }

    private Object convertAnswer(Question question, Answer answer) {
        if (answer.getType().equals(AnswerType.MANUAL)) {
            return answer.getAnswer();
        }

        if (question.getType().isListType()) {
            return (ListUtils.convertList(answer.getAnswer()).stream()
                    .map(optionId -> getOptionValue(question, optionId)).toList());
        }

        return getOptionValue(question, answer.getAnswer());
    }

    private Object getOptionValue(Question question, Object answer) {
        Option option = question.getOption(answer.toString());
        if (question.getType().isNumericType()) {
            return option.getValue();
        }
        return option.getText();
    }

    private Question getQuestionById(String questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_QUESTION_ID));
    }

    private User getUserByWikiId(String wikiId) {
        return userRepository.findByWikiId(wikiId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_USER));
    }
}
