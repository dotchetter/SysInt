package repositories;

import repositories.SensorDAO;
import models.SensorLog;
import models.SensorLogEntry;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;

public class Main {

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException
    {
        //write your test code here
        //SensorDAO dao = new SensorDAO();

	    EmbeddedDeviceHandle deviceHandle = new EmbeddedDeviceHandle("Stockholm");
        Thread threadForDeviceHandle = new Thread(deviceHandle);

        threadForDeviceHandle.start();

        for (;;) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            SensorLog i = StaticDeviceMessageQueue.dequeue();
            //System.out.println("Waiting");
            if (i == null) {continue;}
            SensorLogEntry entry = i.getEntries().get(0);
            System.out.println(i.getSensorType() + " " + entry.getValue() + " " + entry.getCreated() + " " + entry.getCity());
        }

/*
        //Create log entry
        dao.createLogEntry(SensorType.HUMIDITY, new SensorLogEntry(50, new Timestamp(Instant.now().toEpochMilli()), "Uppsala"));

        //Get log entries
        SensorLog log = dao.getLog(SensorType.HUMIDITY, 5);
        for (SensorLogEntry entry : log.getEntries())
        {
            System.out.println(entry.getValue());
            System.out.println(entry.getCreated());
            System.out.println(entry.getCity());
        }*/
    }
}
