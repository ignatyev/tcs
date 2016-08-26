package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        } catch (IOException /*| LogRecordFormatException */e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void collectStatistics(Path logPath) throws InterruptedException {
        try {

            Map<String, Map<String, Record>> groupedByMethodAndId =
//            System.out.println(
                    Files.lines(logPath)
                    .map(Parser::parse)
                    .collect(Collectors.groupingBy(Record::getMethod, Collectors.groupingBy(Record::getId)));
            Stream<Stream<Optional<Record>>> streamStream = groupedByMethodAndId.entrySet().stream().map(
                    stringMapEntry -> stringMapEntry.getValue().entrySet().stream()
                            .map((stringRecordEntry) -> stringMapEntry.getValue().values().stream()
                                    .reduce(
                                            ((record, record2) ->
                                                    Duration.between(record.getTime(), record2.getTime()).toMillis()))));

                   /* forEach(entry -> {
                String method = entry.getKey();
                Map<String, Record> recordMap = entry.getValue();
                recordMap.
            });
*/
        } catch (IOException e) {
            e.printStackTrace();
        }

/*
        try (BufferedReader bufferedReader = Files.newBufferedReader(logPath)) {
            long charsPassed = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) { //EOF
                charsPassed += line.length() + LINE_TERMINATION_CHARS_LEN;
                Record enter = Parser.parse(line);
                if (enter.getAction() == Action.ENTRY) {
                    final long finalCharsPassed = charsPassed;
                    threadPool.submit(() -> {
                        Record exit = getExit(logPath, finalCharsPassed, enter.getId());
                        StatisticsHandler.collect(enter, exit);
                    });
                }
            }
        } catch (IOException | LogRecordFormatException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
            threadPool.awaitTermination(1, TimeUnit.HOURS);
        }*/
    }
}
