package com.dnd.namuiwiki.domain.statistic.model.entity;

import com.dnd.namuiwiki.domain.statistic.model.Statistic;
import com.dnd.namuiwiki.domain.statistic.model.Statistics;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@Document("dashboards")
public class Dashboard {

    @Id
    private String id;

    @DocumentReference(collection = "users", lazy = true)
    private User user;

    private Period period;

    private Relation relation;

    private Statistics statistics;

    public Map<String, Statistic> getStatistics() {
        return statistics.getStatistics();
    }

    public void updateStatistics(List<Survey.Answer> answer) {
        statistics.updateStatistics(answer);
    }
}
