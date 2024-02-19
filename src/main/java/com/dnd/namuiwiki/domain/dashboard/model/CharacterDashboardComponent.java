package com.dnd.namuiwiki.domain.dashboard.model;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.statistic.model.Legend;
import com.dnd.namuiwiki.domain.statistic.model.RatioStatistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistics;
import lombok.Getter;

import java.util.List;

@Getter
public class CharacterDashboardComponent extends DashboardComponent {

    private boolean friendly;
    private boolean similar;
    private boolean mbti;
    private boolean busy;

    public CharacterDashboardComponent(Statistics statistics) {
        super(DashboardType.CHARACTER);
        calculate(statistics);
    }

    @Override
    public void calculate(Statistics statistics) {
        List<Statistic> character = statistics.getStatisticsByDashboardType(this.dashboardType);
        character.forEach(statistic -> {
            RatioStatistic ratioStatistic = (RatioStatistic) statistic;
            List<Legend> legends = ratioStatistic.getLegends();
            QuestionName questionName = ratioStatistic.getQuestionName();

            switch (questionName) {
                case FRIENDLINESS_LEVEL:
                    this.friendly = getLegendByValue(legends, true).getCount() >=
                            getLegendByValue(legends, false).getCount();
                    break;
                case PERSONALITY_TYPE:
                    this.similar = getLegendByValue(legends, true).getCount() >=
                            getLegendByValue(legends, false).getCount();
                    break;
                case MBTI_IMMERSION:
                    this.mbti = getLegendByValue(legends, true).getCount() >=
                            getLegendByValue(legends, false).getCount();
                    break;
                case WEEKEND_COMMITMENTS:
                    this.busy = getLegendByValue(legends, true).getCount() >=
                            getLegendByValue(legends, false).getCount();
                    break;
                default:
                    break;
            }
        });
    }

    private Legend getLegendByValue(List<Legend> legends, boolean value) {
        return legends.stream()
                .filter(legend -> value == (boolean) legend.getValue())
                .findFirst().orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.INTERNAL_ERROR));
    }

}
