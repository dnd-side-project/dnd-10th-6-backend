package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.common.dto.ResponseDto;
import com.dnd.namuiwiki.domain.jwt.JwtAuthorization;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.survey.model.dto.GetAnswersByQuestionResponse;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "답변", description = "Answer API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/answers")
public class AnswerController {
    private final SurveyService surveyService;

    @Operation(summary = "질문별 답변 조회", responses = {
            @ApiResponse(responseCode = "200", description = "질문별 답변 조회 성공", content = @Content(schema = @Schema(implementation = GetAnswersByQuestionResponse.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAnswersByQuestion(
            @JwtAuthorization TokenUserInfoDto tokenUserInfoDto,
            @RequestParam(name = "questionId") String questionId,
            @RequestParam(name = "period", required = false, defaultValue = "TOTAL") Period period,
            @RequestParam(name = "relation", required = false, defaultValue = "TOTAL") Relation relation,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") int pageSize) {

        var answersByQuestion = surveyService.getAnswersByQuestion(tokenUserInfoDto.getWikiId(), questionId, period, relation, pageNo, pageSize);
        return ResponseDto.ok(answersByQuestion);
    }

    @Operation(hidden = true)
    @PutMapping("/set-question-id")
    public ResponseEntity<?> setQuestionIdForSurveyAnswers(@RequestParam String pwd) {
        surveyService.setQuestionIdForSurveyAnswers(pwd);
        return ResponseDto.noContent();
    }

}
