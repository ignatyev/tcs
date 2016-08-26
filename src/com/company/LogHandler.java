package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by AnVIgnatev on 26.08.2016.
 */
public class LogHandler {

    public static final int LINE_TERMINATION_CHARS_LEN = 2;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static Record getExit(Path log, long passed, String id) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(log)) {
            bufferedReader.skip(passed);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Record record = Parser.parse(line);
                if (id.equals(record.getId()) && record.getAction() == Action.EXIT) {
                    return record;
                }
            }
        } catch (IOException | LogRecordFormatException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void collectStatistics(Path logPath) throws InterruptedException {
        //TODO think about hot file changes
        try (BufferedReader bufferedReader = Files.newBufferedReader(logPath)) {
            long passed = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) { //EOF
                passed += line.length() + LINE_TERMINATION_CHARS_LEN;
                Record enter = Parser.parse(line);
                if (enter.getAction() == Action.ENTRY) {
                    final long finalPassed = passed;
                    threadPool.submit(() -> {
                        Record exit = getExit(logPath, finalPassed, enter.getId());
                        StatisticsHandler.collect(enter, exit);
                    });
                }
            }
        } catch (IOException | LogRecordFormatException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
            threadPool.awaitTermination(1, TimeUnit.HOURS);
        }
    }
}
