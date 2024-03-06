package com.dnd.namuiwiki.domain.statistic.model;

import com.dnd.namuiwiki.common.util.ArithmeticUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AverageEntireStatistic extends EntireStatistic {

    private long entireAverage;
    private long peopleCount;


    @Override
    public void updateStatistic(String... args) {
        long newValue = Long.parseLong(args[0]);
        long newAverage = ArithmeticUtils.calculateAverage(peopleCount, entireAverage, newValue);

        increasePeopleCount();
        setEntireAverage(newAverage);
    }

    private void increasePeopleCount() {
        peopleCount++;
    }

    private void setEntireAverage(long borrowingMoneyLimitEntireAverage) {
        this.entireAverage = borrowingMoneyLimitEntireAverage;
    }

}
