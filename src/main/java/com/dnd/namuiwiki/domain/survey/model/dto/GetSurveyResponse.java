package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class GetSurveyResponse {
    private String senderName;
    private Period period;
    private Relation relation;
    //TODO: 설문 작성 날짜 추가
//    private final Date date;
    private List<SingleQuestionAndAnswer> questionAndAnswers;

    @AllArgsConstructor
    private static class SingleQuestionAndAnswer {
        private String questionTitle;
        private Object answer;
        private String reason;

        static SingleQuestionAndAnswer from(String questionTitle, Survey.Answer surveyAnswer) {
            return new SingleQuestionAndAnswer(questionTitle, surveyAnswer.getAnswer(), surveyAnswer.getReason());
        }
    }

    public static GetSurveyResponse from(Survey survey, List<Question> questions) {
        var singleQuestionAndAnswers = pairQuestionAndAnswer(survey, questions);
        return new GetSurveyResponse(survey.getSenderName(), survey.getPeriod(), survey.getRelation(), singleQuestionAndAnswers);
    }

    private static List<SingleQuestionAndAnswer> pairQuestionAndAnswer(Survey survey, List<Question> questions) {
        validateQuestionAndAnswerSize(survey, questions);

        var size = questions.size();
        var answers = survey.getAnswers();
        List<SingleQuestionAndAnswer> questionAndAnswerList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            var question = questions.get(i);
            var answer = answers.get(i);
            questionAndAnswerList.add(SingleQuestionAndAnswer.from(question.getTitle(), answer));
        }
        return questionAndAnswerList;
    }

    private static void validateQuestionAndAnswerSize(Survey survey, List<Question> questions) {
        if (survey.getAnswers().size() != questions.size()) {
            throw new ApplicationErrorException(ApplicationErrorType.QUESTION_ANSWER_COUNT_NOT_EQUAL);
        }
    }
}
