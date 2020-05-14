package repositories;

import models.SensorLogEntry;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SensorDAO extends SqlDAO
{
    public SensorDAO() throws ClassNotFoundException, IOException
    {
    }

    public void createLogEntry(SensorType sensorType, SensorLogEntry entry) throws SQLException
    {
        try
        {
            connect();
            PreparedStatement statement = prepareStatement(
                    "insert into " + sensorType.toString() + "(Value, Created)" +
                            "values(?, ?)"
            );

            statement.setFloat(1, entry.getValue());
            statement.setTimestamp(2, entry.getCreated());
            statement.executeUpdate();
        }
        finally
        {
            disconnect();
        }
    }

    public List<SensorLogEntry> getLogEntry(SensorType sensorType) throws SQLException
    {
        try
        {
            connect();
            ResultSet rs = executeQuery("select * from " + sensorType);
            ArrayList<SensorLogEntry> entries = new ArrayList<>();
            while (rs.next())
                entries.add(
                        new SensorLogEntry(rs.getFloat("Value"), rs.getTimestamp("Created")));
            return entries;
        }
        finally
        {
            disconnect();
        }
    }

}
