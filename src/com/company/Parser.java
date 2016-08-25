package com.company;

import java.time.LocalDateTime;

public class Parser {

    public static final String SPACE = " ";
    public static final int RECORDS_COUNT = 6;

    public static Record parse(String record) throws LogRecordFormatException {
        String[] split = record.split(SPACE);
        if (split.length != RECORDS_COUNT)
            throw new LogRecordFormatException("Wrong record format of " + record + " : wrong length");
        LocalDateTime time = LocalDateTime.parse(split[0]);
        String clazz = split[2];
        Action action = Action.valueOf(split[3].toUpperCase());
        String methodAndId = split[5];
        String method = methodAndId.substring(methodAndId.indexOf("(") + 1, methodAndId.indexOf(":"));
        String id = methodAndId.substring(methodAndId.indexOf(":") + 1, methodAndId.indexOf(")"));
        return new Record(time, clazz, action, method, id);
    }
}
