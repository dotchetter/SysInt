package repositories;

import models.SensorLogEntry;
import java.util.LinkedList;
import java.util.Queue;

public class SensorLogEntryQueue
{
    private Queue<SensorLogEntry> queue = new LinkedList<>();

    public void enqueue(SensorLogEntry logEntry) {
        try {
            queue.add(logEntry);
        } catch (Exception e) {
            System.err.println("Error could not enqueue: " + logEntry);
            e.printStackTrace();
        }
    }

    public SensorLogEntry dequeue() {
        return queue.poll();
    }
}