package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.domain.dashboard.model.dto.RankDto;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.entity.Question;
import com.dnd.namuiwiki.domain.statistic.model.RankStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistic;

import java.util.ArrayList;
import java.util.List;

public class RankDashboardComponent extends DashboardComponentV2 {
    private final List<RankDto> rankList;

    public RankDashboardComponent(DashboardType dashboardType, Statistic statistic, Question question) {
        super(dashboardType, question.getId(), question.getTitle(), question.getName());

        if (!dashboardType.isRankType()) {
            throw new IllegalArgumentException("Required RankDashboardType");
        }

        this.rankList = getRankList((RankStatistic) statistic);
    }

    private List<RankDto> getRankList(RankStatistic rankStatistic) {
        List<RankDto> rankList = new ArrayList<>(rankStatistic.getRankMap().values().stream().toList());
        rankList.sort((o1, o2) -> Double.compare(o2.getPercentage(), o1.getPercentage()));
        return rankList;
    }
}
