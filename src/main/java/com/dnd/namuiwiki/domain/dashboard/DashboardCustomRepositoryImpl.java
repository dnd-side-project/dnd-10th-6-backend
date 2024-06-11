package com.dnd.namuiwiki.domain.dashboard;

import com.dnd.namuiwiki.domain.dashboard.model.entity.Dashboard;
import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@RequiredArgsConstructor
public class DashboardCustomRepositoryImpl implements DashboardCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void updateDashboard(User owner, Period period, Relation relation, List<Answer> answers) {
        Query query = Query.query(Criteria.where("user").is(owner)
                .and("period").is(period)
                .and("relation").is(relation));

        Update update = new Update();

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);

        for (Answer answer : answers) {
            update.inc(String.format("statistics.statistics.%s.totalCount", answer.getQuestion().getId()));

            if (answer.getType().isOption()) {
                update.inc(String.format("statistics.statistics.%s.legends.%s.count",
                        answer.getQuestion().getId(), answer.getAnswer().toString()));
            }
        }

        mongoTemplate.findAndModify(query, update, options, Dashboard.class);
    }

}
