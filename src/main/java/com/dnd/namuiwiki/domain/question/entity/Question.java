package com.dnd.namuiwiki.domain.question.entity;

import com.dnd.namuiwiki.common.exception.ApplicationErrorException;
import com.dnd.namuiwiki.common.exception.ApplicationErrorType;
import com.dnd.namuiwiki.common.model.BaseTimeEntity;
import com.dnd.namuiwiki.domain.dashboard.type.DashboardType;
import com.dnd.namuiwiki.domain.option.entity.Option;
import com.dnd.namuiwiki.domain.question.type.QuestionName;
import com.dnd.namuiwiki.domain.question.type.QuestionType;
import com.dnd.namuiwiki.domain.wiki.WikiType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Map;

@Getter
@Builder
@Document(collection = "questions")
public class Question extends BaseTimeEntity {

    @Id
    private String id;
    private String title;
    private QuestionName name;
    private QuestionType type;
    private DashboardType dashboardType;
    private Long surveyOrder;
    private Long dashboardOrder;
    private WikiType wikiType;
    private boolean reasonRequired;

    @DocumentReference(collection = "options")
    private Map<String, Option> options;

    public Option getOption(String optionId) {
        if (!options.containsKey(optionId)) {
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_OPTION_ID);
        }
        return options.get(optionId);
    }

}
