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
import java.util.List;

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

        static SingleQuestionAndAnswer from(Answer surveyAnswer) {
            Question question = surveyAnswer.getQuestion();

            return new SingleQuestionAndAnswer(
                    question.getTitle(),
                    surveyAnswer.convertToObject(),
                    surveyAnswer.getReason(),
                    question.getName()
            );
        }

    }

    public static GetSurveyResponse from(Survey survey) {
        return new GetSurveyResponse(
                survey.getSenderName(),
                survey.getWikiType(),
                survey.getPeriod(),
                survey.getRelation(),
                survey.getWrittenAt(),
                survey.getAnswers().stream().map(SingleQuestionAndAnswer::from).toList()
        );
    }


}
