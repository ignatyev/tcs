package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * Created by AnVIgnatev on 26.08.2016.
 */
public class LogHandler {
    private static Record getExit(Iterator<String> iteratorExit, String id) throws LogRecordFormatException {
        while (iteratorExit.hasNext()) {
            String recordStr = iteratorExit.next();
            Record record = Parser.parse(recordStr);
            if (id.equals(record.getId()) && record.getAction() == Action.EXIT) {
                return record;
            }
        }
        return null;
    }

    public static void collectStatistics(Path logPath) {
        try {
            //TODO think about hot file changes
            Iterator<String> iteratorEnter = Files.lines(logPath).iterator();
            Iterator<String> iteratorExit = Files.lines(logPath).iterator();
            while (iteratorEnter.hasNext()) {
                String enterStr = iteratorEnter.next();
                Record enter = Parser.parse(enterStr);
                if (enter.getAction() == Action.ENTRY) {
                    Record exit = LogHandler.getExit(iteratorExit, enter.getId());
                    Statistics.collect(enter, exit);
                }
            }
            /*lines.forEach(s -> {
                        Record record = Parser.parse(s);
                        if (record.getAction() == Action.ENTRY) {
                            LogHandler.getExit(, record.getId());
                        }
                    }
            );*/
        } catch (IOException | LogRecordFormatException e) {
            e.printStackTrace();
        }
    }
}