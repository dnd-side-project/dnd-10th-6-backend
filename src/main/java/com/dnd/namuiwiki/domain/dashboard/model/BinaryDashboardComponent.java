package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BinaryDashboardComponent extends DashboardComponent {
    private List<BinaryMetric> characters;

    public BinaryDashboardComponent(DashboardType dashboardType, List<Statistic> statistics) {
        super(dashboardType);
        this.characters = new ArrayList<>();
        initiate(statistics);
    }

    public void initiate(List<Statistic> character) {
        this.characters = character.stream().map(BinaryMetric::from).toList();
    }

    @RequiredArgsConstructor
    @Getter
    static class BinaryMetric {
        private final String name;
        private final boolean value;
        private final String questionId;

        private static BinaryMetric from(Statistic statistic) {
            RatioStatistic ratioStatistic = (RatioStatistic) statistic;
            List<Legend> legends = ratioStatistic.getLegends();
            QuestionName questionName = ratioStatistic.getQuestionName();

            return new BinaryMetric(
                    questionName.name(),
                    getCharacterRatioResult(legends),
                    statistic.getQuestionId()
            );
        }

        private static Legend getLegendByValue(List<Legend> legends, boolean value) {
            return legends.stream()
                    .filter(legend -> value == (boolean) legend.getValue())
                    .findFirst()
                    .orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR));
        }

        private static boolean getCharacterRatioResult(List<Legend> legends) {
            return getLegendByValue(legends, true).getCount() >=
                    getLegendByValue(legends, false).getCount();
        }
    }

}
