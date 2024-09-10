package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.model.dto.RankDto;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.statistic.model.RankStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistic;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class RankDashboardComponent extends DashboardComponentV2 {
    private final List<RankDto> rank;

    public RankDashboardComponent(DashboardType dashboardType, Statistic statistic, Question question) {
        super(dashboardType, question.getId(), question.getTitle(), question.getName(), question.getDashboardOrder());

        if (!dashboardType.isRankType()) {
            throw new IllegalArgumentException("Required RankDashboardType");
        }

        this.rank = getRank((RankStatistic) statistic);
    }

    private List<RankDto> getRank(RankStatistic rankStatistic) {
        AtomicInteger totalPoint = new AtomicInteger();
        rankStatistic.getRanks().forEach((optionId, rank) -> {
            totalPoint.addAndGet(rank.getPoint());
        });

        List<RankDto> rankDtoList = new ArrayList<>();
        rankStatistic.getRanks().forEach((optionId, rank) -> {
            if (totalPoint.get() == 0) {
                return;
            }
            rankDtoList.add(new RankDto(rank.getText(), rank.getPoint(), (rank.getPoint() * 100 / totalPoint.get())));
        });

        rankDtoList.sort((o1, o2) -> Integer.compare(o2.getPercentage(), o1.getPercentage()));
        return rankDtoList;
    }
}
