package com.dnd.namuiwiki.domain.dashboard.model.dto;

import com.dnd.namuiwiki.common.annotation.Enum;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "대시보드 조회 요청 body")
@Getter
public class GetDashboardRequest {

    @Enum(enumClass = Period.class)
    private String period;

    @Enum(enumClass = Relation.class)
    private String relation;

}
