package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.question.QuestionRepository;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.survey.dto.AnswerDto;
import com.dnd.namuiwiki.domain.survey.dto.CreateSurveyRequest;
import com.dnd.namuiwiki.domain.survey.dto.CreateSurveyResponse;
import com.dnd.namuiwiki.domain.survey.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.UserRepository;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;

    public CreateSurveyResponse createSurvey(CreateSurveyRequest request) {
        System.out.println(request);

//        TODO owner
//        User owner = userRepository.findByWikiId(request.getOwner())
//                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_USER));

        // TODO sender: userId -> User
//        User sender = getSender(senderId);

        List<Survey.Answer> answers = makeSurveyAnswers(request.getAnswers());

        Survey survey = Survey.builder()
                .owner(null)
//                .sender(sender)
                .senderName(request.getSenderName())
                .isAnonymous(request.getIsAnonymous())
                .period(Period.valueOf(request.getPeriod()))
                .relation(Relation.valueOf(request.getRelation()))
                .answers(answers)
                .build();

        return new CreateSurveyResponse(surveyRepository.save(survey).getId());
    }

    private User getSender(String senderId) {
        if (senderId == null) {
            return null;
        }
        return userRepository.findById(senderId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_USER));
    }

    private List<Survey.Answer> makeSurveyAnswers(List<AnswerDto> answers) {
        return answers.stream().map(answer -> {
            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_QUESTION_ID));
            return Survey.Answer.builder()
                    .question(question)
                    .type(AnswerType.valueOf(answer.getType()))
                    .answer(answer.getAnswer())
                    .reason(answer.getReason())
                    .build();
        }).toList();
    }

}
