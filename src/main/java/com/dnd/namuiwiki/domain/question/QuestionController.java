package com.dnd.namuiwiki.domain.question;

import com.dnd.namuiwiki.common.annotation.DisableSwaggerSecurity;
import com.dnd.namuiwiki.common.dto.ResponseDto;
import com.dnd.namuiwiki.domain.question.dto.QuestionDto;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "문항", description = "Question API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {
    private final QuestionService questionService;

    @Operation(hidden = true)
    @PostMapping
    public ResponseEntity<?> setDefaultQuestions(
            @RequestParam String pwd,
            @RequestParam WikiType wikiType
    ) {
        questionService.setDefaultQuestions(pwd, wikiType);
        return ResponseDto.noContent();
    }

    @Operation(summary = "설문에 들어갈 문항 조회", responses = {@ApiResponse(responseCode = "200", description = "문항 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestionDto.class))))})
    @DisableSwaggerSecurity
    @GetMapping
    public ResponseEntity<?> getQuestions(
            @RequestParam(required = false, name = "type") QuestionType questionType,
            @RequestParam(name = "wikiType") WikiType wikiType
    ) {
        var response = questionService.getQuestions(questionType, wikiType);
        return ResponseDto.ok(response);
    }

}
