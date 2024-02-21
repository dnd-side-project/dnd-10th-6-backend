package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.dto.ResponseDto;
import com.dnd.namuiwiki.domain.jwt.JwtAuthorization;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/answers")
public class AnswerController {
    private final SurveyService surveyService;

    @GetMapping
    public ResponseEntity<?> getAnswersByQuestion(
            @JwtAuthorization TokenUserInfoDto tokenUserInfoDto,
            @RequestParam(name = "questionId") String questionId,
            @RequestParam(name = "period", required = false, defaultValue = "TOTAL") String period,
            @RequestParam(name = "relation", required = false, defaultValue = "TOTAL") String relation,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize){

        var answersByQuestion = surveyService.getAnswersByQuestion(tokenUserInfoDto.getWikiId(), questionId, Period.valueOf(period), Relation.valueOf(relation), pageNo, pageSize);
        return ResponseDto.ok(answersByQuestion);
    }
}
