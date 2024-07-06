package com.dnd.namuiwiki.domain.dashboard;

import com.dnd.namuiwiki.domain.survey.model.entity.Answer;
import com.dnd.namuiwiki.domain.survey.type.Period;
import com.dnd.namuiwiki.domain.survey.type.Relation;
import com.dnd.namuiwiki.domain.user.entity.User;
import com.dnd.namuiwiki.domain.wiki.WikiType;

import java.util.List;

public interface DashboardCustomRepository {
    void updateDashboard(User owner, WikiType wikiType, Period period, Relation relation, List<Answer> answers);
}
