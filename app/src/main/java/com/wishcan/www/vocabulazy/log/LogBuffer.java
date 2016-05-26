package com.wishcan.www.vocabulazy.log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by allencheng07 on 2016/5/26.
 */
public class LogBuffer {
    private int QUEUE_MAX_SIZE = 100;
    private Queue<LogEntry> queue = new LinkedList<>();

    public void put(String tag, String message) {
        LogEntry entry = new LogEntry(tag, message);
        if (queue.size() == QUEUE_MAX_SIZE)
            queue.remove();
        queue.add(entry);
    }

    public ArrayList<LogEntry> pull() {
        ArrayList<LogEntry> entries = new ArrayList<>();
        while (!queue.isEmpty())
            entries.add(queue.remove());
        return entries;
    }
}
