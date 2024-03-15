package com.dnd.namuiwiki.domain.statistic.model.entity;

import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.statistic.model.EntireStatistic;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document("statistics")
public class PopulationStatistic {

    @Id
    private String id;

    private Period period;

    private Relation relation;

    private QuestionName questionName;

    private DashboardType dashboardType;

    private EntireStatistic statistic;

    public void updateStatistic(String... args) {
        statistic.updateStatistic(args);
    }

    public static PopulationStatistic from(Period period, Relation relation, QuestionName questionName, DashboardType dashboardType) {
        return PopulationStatistic.builder()
                .statistic(EntireStatistic.createEmpty(dashboardType.getStatisticsCalculationType()))
                .dashboardType(dashboardType)
                .period(period)
                .questionName(questionName)
                .relation(relation)
                .build();
    }

}
