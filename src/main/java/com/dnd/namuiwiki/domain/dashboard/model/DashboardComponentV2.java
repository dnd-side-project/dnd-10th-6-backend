package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "대시보드 타입", oneOf = {
        AverageDashboardComponent.class,
        BinaryDashboardComponent.class,
        RatioDashboardComponent.class})
@Getter
@AllArgsConstructor
public abstract class DashboardComponentV2 {
    protected final DashboardType dashboardType;
}
