package com.company;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static final int PARAMS_COUNT = 1;

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != PARAMS_COUNT) {
            System.out.println("Please specify log file absolute path");
            System.exit(0);
        }
        Path logPath = Paths.get(args[0]);
        if (!Files.isReadable(logPath)) {
            System.out.println("Could not read log file by path: " + logPath);
            System.exit(0);
        }
        LogHandler.collectStatistics(logPath);
    }
}
