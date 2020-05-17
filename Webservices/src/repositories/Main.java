package repositories;

import models.SensorLogEntry;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException
    {
	    // write your test code here
        SensorDAO dao = new SensorDAO();

        //Create log entry
        dao.createLogEntry(new SensorLogEntry(50, new Timestamp(Instant.now().toEpochMilli()), "Uppsala", SensorType.HUMIDITY));

        //Get log entries
        List<SensorLogEntry> entries = dao.getLogEntries(SensorType.HUMIDITY, 5);
        for (SensorLogEntry entry : entries)
        {
            System.out.println(entry.getValue());
            System.out.println(entry.getCreated());
            System.out.println(entry.getCity());
        }
    }
}
