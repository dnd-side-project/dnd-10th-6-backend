package com.dnd.namuiwiki.domain.dashboard.model.entity;

import com.dnd.namuiwiki.common.model.BaseTimeEntity;
import com.dnd.namuiwiki.domain.dashboard.model.DashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.DashboardFactory;
import com.dnd.namuiwiki.domain.dashboard.model.Statistic;
import com.dnd.namuiwiki.domain.dashboard.model.Statistics;
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

    public List<DashboardComponent> getUserDashboards() {
        var dashboardTypeListMap = statistics.mapStatisticsByDashboardType();

        return dashboardTypeListMap.entrySet().stream()
                .filter(entry -> entry.getKey().getAnalysisType().isUser())
                .map(entry -> DashboardFactory.create(entry.getKey(), entry.getValue()))
                .toList();
    }

    public DashboardComponent getPopulationDashboard(PopulationStatistic populationStatistic, DashboardType dashboardType) {
        List<Statistic> statisticsByDashboardType = statistics.getStatisticsByDashboardType(dashboardType);

        return DashboardFactory.create(dashboardType, statisticsByDashboardType, populationStatistic);
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
