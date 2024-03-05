package com.dnd.namuiwiki.domain.dashboard.model.entity;

import com.dnd.namuiwiki.common.model.BaseTimeEntity;
import com.dnd.namuiwiki.domain.statistic.model.Statistics;
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

}
