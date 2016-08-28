package com.company.parser;

import java.time.LocalDateTime;

public class Record {
    private final LocalDateTime time;
    private final Action action;
    private final String method;
    private final String id;

    public Record(LocalDateTime time, Action action, String method, String id) {
        this.time = time;
        this.action = action;
        this.method = method;
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
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
