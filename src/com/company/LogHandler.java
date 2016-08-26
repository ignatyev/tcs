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


        /*while (iteratorExit.hasNext()) {
            String recordStr = iteratorExit.next();
            Record record = Parser.parse(recordStr);
            if (id.equals(record.getId()) && record.getAction() == Action.EXIT) {
                return record;
            }
        }*/
        return null;
    }

    public static void collectStatistics(Path logPath) {
        /*//TODO think about hot file changes
        Iterator<String> iteratorEnter = Files.lines(logPath).iterator();
        Iterator<String> iteratorExit = Files.lines(logPath).iterator();
        while (iteratorEnter.hasNext()) {
            String enterStr = iteratorEnter.next();
            Record enter = Parser.parse(enterStr);
            if (enter.getAction() == Action.ENTRY) {
                Record exit = LogHandler.getExit(iteratorExit, enter.getId());
                StatisticsHandler.collect(enter, exit);
            }
        }*/
//                FileReader fileReader = new FileReader(logPath.toFile());
//        StringReader stringReader = new StringReader();
//                stringReader.re
        try (BufferedReader bufferedReader = Files.newBufferedReader(logPath)) {
            long passed = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) { //EOF
                passed += line.length() + LINE_TERMINATION_CHARS_LEN; //FIXME last line
                Record enter = Parser.parse(line);
                if (enter.getAction() == Action.ENTRY) {
//                bufferedReader.mark(0);//TODO counter+skip?
                    final long finalPassed = passed;
                    threadPool.submit(() -> {
                        Record exit = getExit(logPath, finalPassed, enter.getId());
                        StatisticsHandler.collect(enter, exit);
                    });
                }
            }
            threadPool.awaitTermination(1, TimeUnit.HOURS);
            threadPool.shutdown();

        } catch (IOException | LogRecordFormatException | InterruptedException e) {
            e.printStackTrace();
        }

            /*lines.forEach(s -> {
                        Record record = Parser.parse(s);
                        if (record.getAction() == Action.ENTRY) {
                            LogHandler.getExit(, record.getId());
                        }
                    }
            );*/
    }
}
