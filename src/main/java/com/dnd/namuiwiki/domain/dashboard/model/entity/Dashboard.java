package com.dnd.namuiwiki.domain.dashboard.model.entity;

import com.dnd.namuiwiki.common.model.BaseTimeEntity;
import com.dnd.namuiwiki.domain.dashboard.model.BestWorthDashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.CharacterDashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.DashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.HappyDashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.MoneyDashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.SadDashboardComponent;
import com.dnd.namuiwiki.domain.dashboard.model.Statistics;
import com.dnd.namuiwiki.domain.dashboard.model.dto.DashboardDto;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.statistic.model.BorrowingLimitEntireStatistic;
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
        BorrowingLimitEntireStatistic statistic = (BorrowingLimitEntireStatistic) populationStatistic.getStatistic();
        long entireAverage = statistic.getBorrowingMoneyLimitEntireAverage();

        List<DashboardComponent> dashboardComponents = List.of(
                new BestWorthDashboardComponent(statistics.getStatisticsByDashboardType(DashboardType.BEST_WORTH)),
                new HappyDashboardComponent(statistics.getStatisticsByDashboardType(DashboardType.HAPPY)),
                new SadDashboardComponent(statistics.getStatisticsByDashboardType(DashboardType.SAD)),
                new CharacterDashboardComponent(statistics.getStatisticsByDashboardType(DashboardType.CHARACTER)),
                new MoneyDashboardComponent(statistics.getStatisticsByDashboardType(DashboardType.MONEY), entireAverage)
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
