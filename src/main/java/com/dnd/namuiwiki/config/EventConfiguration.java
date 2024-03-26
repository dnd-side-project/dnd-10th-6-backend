package com.dnd.namuiwiki.config;

import com.dnd.namuiwiki.domain.statistic.StatisticsService;
import com.dnd.namuiwiki.domain.survey.SurveyEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EventConfiguration {
    private final StatisticsService statisticsService;

    @Bean
    public SurveyEventHandler surveyEventHandler() {
        return new SurveyEventHandler(statisticsService);
    }

}
