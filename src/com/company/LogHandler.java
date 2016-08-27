package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Created by AnVIgnatev on 26.08.2016.
 */
public class LogHandler {

    public static void collectStatistics(Path logPath) throws InterruptedException {
        try {

            Map<String, Map<String, List<Record>>> groupedByMethodAndId =
                    Files.lines(logPath)
                            .map(Parser::parse)
                            .collect(Collectors.groupingBy(Record::getMethod, Collectors.groupingBy(Record::getId)));
//            System.out.println(groupedByMethodAndId);
            Map<String, Map<String, Long>> methodsDurations = new HashMap<>();
            groupedByMethodAndId.entrySet().forEach(stringMapEntry -> {
                String method = stringMapEntry.getKey();
//                System.out.println(method);
                methodsDurations.put(method, new HashMap<>());
                stringMapEntry.getValue().entrySet().forEach(stringListEntry -> {
                    String id = stringListEntry.getKey();
                    List<Record> records = stringListEntry.getValue();
                    OptionalLong duration = records.stream()
                            .mapToLong(record -> record.getTime().toInstant(ZoneOffset.UTC).toEpochMilli())
                            .reduce((enter, exit) -> Math.abs(enter - exit)); //FIXME if there's no exit
                    duration.ifPresent(value -> methodsDurations.get(method).put(id, value));
                });
            });
//            System.out.println(methodsDurations);
            methodsDurations.entrySet().forEach(stringMapEntry -> {
                Optional<Map.Entry<String, Long>> max = stringMapEntry.getValue().entrySet().stream()
                        .max((o1, o2) -> (int) (o1.getValue() - o2.getValue()));
                LongStream longStream = stringMapEntry.getValue().entrySet().stream()
                        .mapToLong(Map.Entry::getValue);
                System.out.println(stringMapEntry.getKey());
                max.ifPresent((entry) -> System.out.println(entry.getKey()));
                System.out.println(longStream.summaryStatistics());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
