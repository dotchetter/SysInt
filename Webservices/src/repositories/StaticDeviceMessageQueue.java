package repositories;

import models.SensorLogEntry;
import java.util.LinkedList;
import java.util.Queue;

class StaticDeviceMessageQueue {

    private static Queue<SensorLogEntry> logQueue = new LinkedList<>();

    public static void enqueue(SensorLogEntry logEntry) {
        try {
            logQueue.add(logEntry);
        } catch (Exception e) {
            System.err.println("Error could not enqueue: " + logEntry);
            e.printStackTrace();
        }
    }

    public static SensorLogEntry dequeue() {
        return logQueue.poll();
    }
}

class testQueue {
    public static void main(String[] args) {
    }
}
