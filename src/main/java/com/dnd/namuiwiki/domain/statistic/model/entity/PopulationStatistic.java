package com.dnd.namuiwiki.domain.statistic.model.entity;

import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.statistic.model.EntireStatistic;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document("statistics")
public class PopulationStatistic {

    @Version
    private Long version;

    @Id
    private String id;

    private Period period;

    private Relation relation;

    private QuestionName questionName;

    private EntireStatistic statistic;

}
