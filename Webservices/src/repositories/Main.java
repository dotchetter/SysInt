package repositories;

import models.SensorLog;
import models.SensorLogEntry;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;

public class Main {

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException
    {
	    // write your test code here
        SensorDAO dao = new SensorDAO();

        //Create log entry
        dao.createLogEntry(SensorType.HUMIDITY, new SensorLogEntry(50, new Timestamp(Instant.now().toEpochMilli()), "Uppsala"));

        //Get log entries
        SensorLog log = dao.getLog(SensorType.HUMIDITY, 5);
        for (SensorLogEntry entry : log.getEntries())
        {
            System.out.println(entry.getValue());
            System.out.println(entry.getCreated());
            System.out.println(entry.getCity());
        }
    }
}
