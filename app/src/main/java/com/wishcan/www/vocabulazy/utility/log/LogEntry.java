package com.wishcan.www.vocabulazy.utility.log;

public class LogEntry {
    private String tag;
    private String message;

    public LogEntry(String tag, String message) {
        this.tag = tag;
        this.message = message;
    }

    public String getTag() {
        return tag;
    }

    public String getMessage() {
        return message;
    }
}
