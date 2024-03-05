package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "대시보드 타입", oneOf = {
        BestWorthDashboardComponent.class,
        CharacterDashboardComponent.class,
        HappyDashboardComponent.class,
        MoneyDashboardComponent.class,
        SadDashboardComponent.class})
@AllArgsConstructor
@Getter
public abstract class DashboardComponent {

    protected final DashboardType dashboardType;

    public abstract void calculate(Statistics statistics);

}
