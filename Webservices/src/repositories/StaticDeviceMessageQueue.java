package repositories;

import models.SensorLog;
import models.SensorLogEntry;
import java.util.LinkedList;
import java.util.Queue;

public class StaticDeviceMessageQueue {

    private static Queue<SensorLog> logQueue = new LinkedList<>();

    public static void enqueue(SensorLog logEntry) {
        try {
            logQueue.add(logEntry);
        } catch (Exception e) {
            System.err.println("Error could not enqueue: " + logEntry);
            e.printStackTrace();
        }
    }

    public static SensorLog dequeue() {
        return logQueue.poll();
    }
}