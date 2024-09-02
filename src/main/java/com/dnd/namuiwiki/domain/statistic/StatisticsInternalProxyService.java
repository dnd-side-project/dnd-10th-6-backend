package com.dnd.namuiwiki.domain.statistic;


import com.dnd.namuiwiki.domain.statistic.model.BorrowingLimitEntireStatistic;
import com.dnd.namuiwiki.domain.statistic.model.entity.PopulationStatistic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatisticsInternalProxyService {
    private final StatisticsRepository statisticsRepository;

    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 30 * 1000L)
    )
    public void updateBorrowingLimitStatisticByCategory(
            PopulationStatistic populationStatistic,
            long borrowingLimit
    ) {
        BorrowingLimitEntireStatistic statistic = (BorrowingLimitEntireStatistic) populationStatistic.getStatistic();
        statistic.updateStatistic(String.valueOf(borrowingLimit));

        populationStatistic.setStatistic(statistic);

        statisticsRepository.save(populationStatistic);
    }

    @Recover
    private void recoverForUpdateStatistics(Exception e) {
        log.error("Failed to update statistics", e);
    }

}
