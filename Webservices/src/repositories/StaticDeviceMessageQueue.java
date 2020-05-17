package repositories;

import models.SensorLogEntry;

import java.util.Queue;

public class StaticDeviceMessageQueue {

    private static Queue<SensorLogEntry> logQueue;

    public static void enqueue(SensorLogEntry logEntry) {
        try {
            logQueue.add(logEntry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SensorLogEntry dequeue() {
        return logQueue.poll();
    }
}
