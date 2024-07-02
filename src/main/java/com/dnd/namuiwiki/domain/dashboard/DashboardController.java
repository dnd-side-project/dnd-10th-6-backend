package com.dnd.namuiwiki.domain.dashboard;

import com.dnd.namuiwiki.common.dto.ResponseDto;
import com.dnd.namuiwiki.domain.dashboard.model.dto.DashboardDto;
import com.dnd.namuiwiki.domain.jwt.JwtAuthorization;
import com.dnd.namuiwiki.domain.jwt.dto.TokenUserInfoDto;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "대시보드", description = "Dashboard API")
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @Operation(summary = "대시 보드 조회", responses = {
            @ApiResponse(responseCode = "200", description = "대시보드 조회 성공", content = @Content(schema = @Schema(implementation = DashboardDto.class))),
    })
    @GetMapping
    public ResponseEntity<?> getDashboard(
            @JwtAuthorization TokenUserInfoDto tokenUserInfoDto,
            @RequestParam(required = false) Period period,
            @RequestParam(required = false) Relation relation,
            @RequestParam WikiType wikiType
    ) {
        DashboardDto response = dashboardService.getDashboard(tokenUserInfoDto, wikiType, period, relation);
        return ResponseDto.ok(response);
    }

}
