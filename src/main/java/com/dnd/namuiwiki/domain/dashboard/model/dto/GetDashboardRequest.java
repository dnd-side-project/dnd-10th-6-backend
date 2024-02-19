package com.dnd.namuiwiki.domain.dashboard.model.dto;

import com.dnd.namuiwiki.common.annotation.Enum;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import lombok.Getter;

@Getter
public class GetDashboardRequest {

    @Enum(enumClass = Period.class)
    private String period;

    @Enum(enumClass = Relation.class)
    private String relation;

}
