package com.dnd.namuiwiki.domain.dashboard.model.dto;

import com.dnd.namuiwiki.domain.dashboard.model.DashboardComponent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "대시보드 응답 body")
@AllArgsConstructor
@Getter
public class DashboardDto {
    private List<DashboardComponent> statistics;
}
