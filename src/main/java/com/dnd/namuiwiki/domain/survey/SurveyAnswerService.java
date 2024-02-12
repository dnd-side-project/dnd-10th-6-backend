package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.option.OptionRepository;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.QuestionRepository;
import com.dnd.namuiwiki.domain.question.dto.QuestionDto;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.survey.model.SurveyAnswer;
import com.dnd.namuiwiki.domain.survey.model.dto.AnswerDto;
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
        var answers = answersRequest.stream().map(answer -> {
            Question question = getQuestionById(answer.getQuestionId());
            AnswerType answerType = AnswerType.valueOf(answer.getType());

            if (answerType.isOption()) {
                String optionId = answer.getAnswer().toString();
                validateOptionExists(optionId, question);
            }

            return SurveyAnswer.create(QuestionDto.from(question), answerType, answer.getAnswer(), answer.getReason());
        }).toList();

        return new SurveyAnswer(answers);
    }

    private void validateOptionExists(String optionId, Question question) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_OPTION_ID));

        if (!question.getOptions().contains(option)) {
            throw new ApplicationErrorException(ApplicationErrorType.CONFLICT_OPTION_QUESTION);
        }
    }

    private Question getQuestionById(String questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INVALID_QUESTION_ID));
    }

}
