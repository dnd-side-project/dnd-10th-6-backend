package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.option.OptionRepository;
import com.dnd.namuiwiki.domain.question.QuestionRepository;
import com.dnd.namuiwiki.domain.question.dto.QuestionDto;
import com.dnd.namuiwiki.domain.survey.model.dto.AnswerDto;
import com.dnd.namuiwiki.domain.survey.model.SurveyAnswer;
import com.dnd.namuiwiki.domain.survey.type.AnswerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyAnswerService {
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    public SurveyAnswer getSurveyAnswers(List<AnswerDto> answersRequest) {
        return new SurveyAnswer(answersRequest.stream()
                .map(answer -> {
                    QuestionDto question = getQuestionById(answer.getQuestionId());
                    AnswerType answerType = AnswerType.valueOf(answer.getType());

                    if (answerType.isOption()) {
                        validateOptionExists(answer);
                    }

                    return SurveyAnswer.createSurveyAnswer(
                            question,
                            answerType,
                            answer.getAnswer(),
                            answer.getReason());
                })
                .toList());
    }

    private void validateOptionExists(AnswerDto answer) {
        if (!optionRepository.existsById(answer.getAnswer())) {
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_OPTION_ID);
        }
    }

    private QuestionDto getQuestionById(String questionId) {
        return QuestionDto.from(questionRepository.findById(questionId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_QUESTION_ID)));
    }

}
