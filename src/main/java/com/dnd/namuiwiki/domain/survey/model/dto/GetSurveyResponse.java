package com.dnd.namuiwiki.domain.survey.model.dto;

import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class GetSurveyResponse {
    private String senderName;
    private WikiType wikiType;
    private Period period;
    private Relation relation;
    private LocalDateTime createdAt;
    private List<SingleQuestionAndAnswer> questionAndAnswers;

    @Getter
    @AllArgsConstructor
    private static class SingleQuestionAndAnswer {
        private String questionTitle;
        private Object answer;
        private String reason;
        private QuestionName questionName;

        static SingleQuestionAndAnswer from(Question question, Answer surveyAnswer) {
            return new SingleQuestionAndAnswer(
                    question.getTitle(),
                    surveyAnswer.convertToObject(),
                    surveyAnswer.getReason(),
                    question.getName()
            );
        }

    }

    public static GetSurveyResponse from(Survey survey, List<Question> questions) {
        var singleQuestionAndAnswers = pairQuestionAndAnswer(survey, questions);
        return new GetSurveyResponse(survey.getSenderName(), survey.getWikiType(), survey.getPeriod(), survey.getRelation(), survey.getWrittenAt(), singleQuestionAndAnswers);
    }

    private static List<SingleQuestionAndAnswer> pairQuestionAndAnswer(Survey survey, List<Question> questions) {
        Map<String, Question> questionMap = new HashMap<>();
        questions.forEach(question -> questionMap.put(question.getId(), question));

        var answers = survey.getAnswers();
        List<SingleQuestionAndAnswer> questionAndAnswerList = new ArrayList<>();
        answers.forEach(answer -> questionAndAnswerList.add(SingleQuestionAndAnswer.from(questionMap.get(answer.getQuestion().getId()), answer)));
        return questionAndAnswerList;
    }

}
