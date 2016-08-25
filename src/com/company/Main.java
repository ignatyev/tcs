package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

    public static final int PARAMS_COUNT = 1;

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != PARAMS_COUNT) System.out.println("Please specify log file absolute path");
        Path logPath = Paths.get(args[0]);
        if (!Files.exists(logPath)) System.out.println("Could not find log file by path: " + logPath);
//        FileReader fileReader = new FileReader(logPath.toFile());
//        StringReader stringReader = new StringReader();
//                stringReader.re

        try (BufferedReader br = Files.newBufferedReader(logPath)) {
            Stream<String> lines = br.lines();
            lines.forEach(s -> {
                        Record record = Parser.parse(s);
                        if (record.getAction() == Action.ENTRY) {
                            
                        }
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try (BufferedReader bufferedReader = Files.newBufferedReader(logPath)) {
            String s = bufferedReader.readLine();
            Record record = Parser.parse(s);
        } catch (IOException | LogRecordFormatException e) {
            e.printStackTrace();
        }*/
    }
}
