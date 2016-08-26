package com.company;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by AnVIgnatev on 26.08.2016.
 */
public class StatisticsHandler {
    private static Map<String, Stats> statsMap = new ConcurrentHashMap<>();

    public static void collect(Record enter, Record exit) {
        if (exit == null || enter == null) return;
        long durationMillis = Duration.between(enter.getTime(), exit.getTime()).toMillis();
        String method = enter.getMethod();
        Stats stats = statsMap.putIfAbsent(method,
                new Stats(durationMillis, durationMillis, durationMillis, 1, durationMillis, enter.getId()));
        if (stats == null) {
            return;
        }

        synchronized (stats) {
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
    }

    public static Map<String, Stats> getStatistics() {
        return Collections.unmodifiableMap(statsMap);
    }
}
