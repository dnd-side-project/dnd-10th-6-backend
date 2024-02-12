package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.option.OptionRepository;
import com.dnd.namuiwiki.domain.question.QuestionRepository;
import com.dnd.namuiwiki.domain.survey.model.dto.CreateSurveyRequest;
import com.dnd.namuiwiki.domain.survey.model.dto.CreateSurveyResponse;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.model.SurveyAnswers;
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
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final OptionRepository optionRepository;

    public CreateSurveyResponse createSurvey(CreateSurveyRequest request, SurveyAnswers surveyAnswers) {
        System.out.println(request);

//        TODO owner
//        User owner = userRepository.findByWikiId(request.getOwner())
//                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.NOT_FOUND_USER));

        // TODO sender: userId -> User
//        User sender = getSender(senderId);

        Survey survey = Survey.builder()
                .owner(null)
//                .sender(sender)
                .senderName(request.getSenderName())
                .isAnonymous(request.getIsAnonymous())
                .period(Period.valueOf(request.getPeriod()))
                .relation(Relation.valueOf(request.getRelation()))
                .answers(surveyAnswers.toEntity())
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

}
