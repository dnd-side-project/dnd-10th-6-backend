package com.dnd.namuiwiki.domain.statistic.model;

import com.dnd.namuiwiki.common.util.ArithmeticUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BorrowingLimitEntireStatistic implements EntireStatistic {

    private long borrowingMoneyLimitEntireAverage;
    private long peopleCount;


    @Override
    public void updateStatistic(String... args) {
        long borrowingLimit = Long.parseLong(args[0]);
        long newAverage = ArithmeticUtils.calculateAverage(peopleCount, borrowingMoneyLimitEntireAverage, borrowingLimit);

        increasePeopleCount();
        setBorrowingMoneyLimitEntireAverage(newAverage);
    }

    private void increasePeopleCount() {
        peopleCount++;
    }

    private void setBorrowingMoneyLimitEntireAverage(long borrowingMoneyLimitEntireAverage) {
        this.borrowingMoneyLimitEntireAverage = borrowingMoneyLimitEntireAverage;
    }

}
