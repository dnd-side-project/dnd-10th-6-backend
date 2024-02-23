package com.dnd.namuiwiki.common.util;

public class ArithmeticUtils {

    public static long calculateAverage(long count, long average, long newNumber) {
        if (count == 0) {
            return newNumber;
        }
        double a = (double) count / (count + 1);
        long b = average + newNumber / count;
        return (long) (a * b);
    }

}
