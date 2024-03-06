package com.dnd.namuiwiki.domain.dashboard.model.entity;

import com.dnd.namuiwiki.common.model.BaseTimeEntity;
import com.dnd.namuiwiki.domain.dashboard.model.DashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.DashboardFactory;
import com.dnd.namuiwiki.domain.dashboard.model.Statistics;
import com.dnd.namuiwiki.domain.dashboard.model.dto.DashboardDto;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.statistic.model.entity.PopulationStatistic;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Builder
@Document("dashboards")
public class Dashboard extends BaseTimeEntity {

    @Id
    private String id;

    @DocumentReference(collection = "users", lazy = true)
    private User user;

    private Period period;

    private Relation relation;

    private Statistics statistics;

    public void updateStatistics(List<Answer> answer) {
        statistics.updateStatistics(answer);
    }

    public DashboardDto convertDashboardDto(PopulationStatistic populationStatistic) {
        var dashboardTypeListMap = statistics.mapStatisticsByDashboardType();

        List<DashboardComponent> dashboardComponents = List.of(
                DashboardFactory.create(DashboardType.BEST_WORTH, dashboardTypeListMap.get(DashboardType.BEST_WORTH)),
                DashboardFactory.create(DashboardType.SAD, dashboardTypeListMap.get(DashboardType.SAD)),
                DashboardFactory.create(DashboardType.HAPPY, dashboardTypeListMap.get(DashboardType.HAPPY)),
                DashboardFactory.create(DashboardType.CHARACTER, dashboardTypeListMap.get(DashboardType.CHARACTER)),
                DashboardFactory.create(DashboardType.MONEY, dashboardTypeListMap.get(DashboardType.MONEY), populationStatistic)
        );
        return new DashboardDto(dashboardComponents);
    }

    public static Dashboard createNew(User owner, Period period, Relation relation, List<Answer> answers) {
        return Dashboard.builder()
                .user(owner)
                .period(period)
                .relation(relation)
                .statistics(Statistics.from(answers.stream().map(Answer::getQuestion).toList()))
                .build();
    }

}
