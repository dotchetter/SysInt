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
        dao.createLogEntry(SensorType.HUMIDITY, new SensorLogEntry(50, new Timestamp(Instant.now().toEpochMilli())));
        List<SensorLogEntry> entries = dao.getLogEntry(SensorType.HUMIDITY);
        for (SensorLogEntry entry : entries)
        {
            System.out.println(entry.getValue());
            System.out.println(entry.getCreated());
        }
    }
}
