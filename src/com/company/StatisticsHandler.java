package com.company;

import java.time.Duration;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AnVIgnatev on 26.08.2016.
 */
public class StatisticsHandler {
    private static Map<String, Stats> statsMap = new HashMap<>();

    public static void collect(Record enter, Record exit) {
        if (exit == null || enter == null) return;
        long durationMillis = Duration.between(enter.getTime(), exit.getTime()).toMillis();
        String method = enter.getMethod();
        Stats stats = statsMap.get(method);
        if (stats == null) {
            statsMap.put(method, new Stats(durationMillis, durationMillis, durationMillis, 1, durationMillis, enter.getId()));
            return;
        }

        stats.setCount(stats.getCount() + 1);
        stats.setSum(stats.getSum() + durationMillis);
        stats.setAvg((float) stats.getSum() / stats.getCount());
        if (durationMillis < stats.getMin()) {
            stats.setMin(durationMillis);
        }
        if (durationMillis > stats.getMax()) {
            stats.setMax(durationMillis);
            stats.setIdOfMax(enter.getId());
        }

    }

    public static Map<String, Stats> getStatistics() {
        return Collections.unmodifiableMap(statsMap);
    }
}
