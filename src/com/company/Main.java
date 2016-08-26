package com.company;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static final int PARAMS_COUNT = 1;

    public static void main(String[] args) throws FileNotFoundException {
        LocalDateTime start = LocalDateTime.now();
        if (args.length != PARAMS_COUNT) {
            System.out.println("Please specify log file absolute path");
            System.exit(0);
        }
        Path logPath = Paths.get(args[0]);
        if (!Files.isReadable(logPath)) {
            System.out.println("Could not read log file by path: " + logPath);
            System.exit(0);
        }
        try {
            LogHandler.collectStatistics(logPath);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Work time: " + Duration.between(start, LocalDateTime.now()).toMillis());
        System.out.println(StatisticsHandler.getStatistics());
    }
}
