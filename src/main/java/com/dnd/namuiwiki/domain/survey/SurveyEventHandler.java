package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.domain.dashboard.DashboardService;
import com.dnd.namuiwiki.domain.statistic.StatisticsService;
import com.dnd.namuiwiki.domain.survey.model.dto.SurveyCreatedEvent;
import com.dnd.namuiwiki.domain.survey.model.entity.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
public class SurveyEventHandler {
    private final StatisticsService statisticsService;
    private final DashboardService dashboardService;

    @Async
    @EventListener
    public void handleSurveySuccessEvent(SurveyCreatedEvent event) {
        Survey survey = event.getSurvey();
        log.info("SurveyEventHandler.handleSurveySuccessEvent: surveyId={}", survey.getId());

        statisticsService.updateStatistics(survey);
        dashboardService.updateDashboards(survey);
    }

}
