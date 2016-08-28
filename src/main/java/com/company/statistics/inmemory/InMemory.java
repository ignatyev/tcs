package com.company.statistics.inmemory;

import com.company.parser.Parser;
import com.company.parser.Record;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class InMemory {
    public static void printStatistics(Path logPath) {
        Map<String, Map<String, List<Record>>> groupedByMethodAndId = new HashMap<>();
        try {
            groupedByMethodAndId = Files.lines(logPath)
                    .map(Parser::parse)
                    .collect(Collectors.groupingBy(Record::getMethod, Collectors.groupingBy(Record::getId)));
        } catch (IOException e) {
            e.printStackTrace();
        }
//            System.out.println(groupedByMethodAndId);
        Map<String, Map<String, Long>> methodsDurations = new HashMap<>();
        groupedByMethodAndId.entrySet().forEach(stringMapEntry -> {
            String method = stringMapEntry.getKey();
//                System.out.println(method);
            methodsDurations.put(method, new HashMap<>());
            stringMapEntry.getValue().entrySet().forEach(stringListEntry -> {
                String id = stringListEntry.getKey();
                List<Record> records = stringListEntry.getValue();
                if (records.size() != 2) return; //enter or end is absent - skipping the record
                OptionalLong duration = records.stream()
                        .mapToLong(record -> record.getTime().toInstant(ZoneOffset.UTC).toEpochMilli())
                        .reduce((enter, exit) -> Math.abs(enter - exit));
                duration.ifPresent(value -> methodsDurations.get(method).put(id, value));
            });
        });
//            System.out.println(methodsDurations);
        methodsDurations.entrySet().forEach(stringMapEntry -> {
            Optional<Map.Entry<String, Long>> max = stringMapEntry.getValue().entrySet().stream()
                    .max((o1, o2) -> (int) (o1.getValue() - o2.getValue()));
            LongStream durations = stringMapEntry.getValue().entrySet().stream()
                    .mapToLong(Map.Entry::getValue);

            LongSummaryStatistics statistics = durations.summaryStatistics();
            System.out.println(String.format("%s min %d, max %d, avg %s, max id %s, count %d",
                    stringMapEntry.getKey(),
                    statistics.getMin(),
                    statistics.getMax(),
                    statistics.getAverage(),
                    max.isPresent() ? max.get().getKey() : null,
                    statistics.getCount()
            ));
        });
    }
}
