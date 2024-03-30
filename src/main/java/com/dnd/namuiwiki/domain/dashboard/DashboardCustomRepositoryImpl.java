package com.dnd.namuiwiki.domain.dashboard;

import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@RequiredArgsConstructor
public class DashboardCustomRepositoryImpl implements DashboardCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void updateDashboard(User owner, Period period, Relation relation, List<Answer> answers) {
    }

}
