package com.company;

import java.time.LocalDateTime;

public class Record {
    private final LocalDateTime time;
    private final String clazz;
    private final Action action;
    private final String method;
    private final String id;

    public Record(LocalDateTime time, String clazz, Action action, String method, String id) {
        this.time = time;
        this.clazz = clazz;
        this.action = action;
        this.method = method;
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getClazz() {
        return clazz;
    }

    public Action getAction() {
        return action;
    }

    public String getMethod() {
        return method;
    }

    public String getId() {
        return id;
    }

}
