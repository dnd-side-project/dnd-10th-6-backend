package com.dnd.namuiwiki.domain.dashboard.model.dto;

import com.dnd.namuiwiki.domain.dashboard.model.DashboardComponent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class DashboardDto {
    private List<DashboardComponent> statistics;
}
