package com.company;

import com.company.statistics.InMemory;
import com.company.statistics.linebyline.LineByLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by AnVIgnatev on 26.08.2016.
 */
public class LogHandler {

    public static void printStatistics(Path logPath) throws IOException {
        if (Runtime.getRuntime().maxMemory() > Files.size(logPath)) {
            InMemory.printStatistics(logPath);
        } else {
            LineByLine.printStatistics(logPath);
        }
    }
}
