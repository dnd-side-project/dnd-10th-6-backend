package com.dnd.namuiwiki.domain.survey;

import com.dnd.namuiwiki.domain.dashboard.DashboardRepository;
import com.dnd.namuiwiki.domain.dashboard.DashboardService;
import com.dnd.namuiwiki.domain.statistic.StatisticsService;
import com.dnd.namuiwiki.domain.survey.model.dto.CreateSurveySuccessEvent;
import com.dnd.namuiwiki.domain.survey.model.dto.ResetDashboardEvent;
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

    private final SurveyRepository surveyRepository;
    private final DashboardRepository dashboardRepository;

    @Async
    @EventListener
    public void handleSurveySuccessEvent(CreateSurveySuccessEvent event) {
        Survey survey = event.getSurvey();
        log.info("SurveyEventHandler.handleSurveySuccessEvent: surveyId={}", survey.getId());

        dashboardService.updateStatistics(survey);
        statisticsService.updateStatistics(survey);
    }

    @Async
    @EventListener
    public void handleResetDashboardEvent(ResetDashboardEvent event) {
        log.info("SurveyHandler.handleResetDashboardEvent");

        dashboardRepository.deleteAll();
        surveyRepository.findAll().forEach(dashboardService::updateStatistics);

        log.info("SurveyHandler.handleResetDashboardEvent done");
    }

}
