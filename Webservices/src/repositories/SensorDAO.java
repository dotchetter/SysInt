package repositories;

import models.SensorLog;
import models.SensorLogEntry;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SensorDAO extends SqlDAO
{
    public SensorDAO() throws IOException, ClassNotFoundException
    {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    }

    public void createLogEntry(SensorType sensorType, SensorLogEntry entry) throws SQLException
    {
        try
        {
            connect();
            PreparedStatement statement = prepareStatement(
                   "declare @cityName nvarchar(50) = ?;" +
                        "declare @cityId int;" +
                        "if not exists (select 1 from City where Name = @cityName)" +
                        "   insert into City(Name) values(@cityName);" +
                        "select @cityId = Id from City where Name = @cityName;" +
                        "insert into " + sensorType + "(Value, Created, CityId) values(?, ?, @cityId);"
            );

            statement.setString(1, entry.getCity());
            statement.setFloat(2, entry.getValue());
            statement.setTimestamp(3, entry.getCreated());
            statement.executeUpdate();
        }
        finally
        {
            disconnect();
        }
    }

    public SensorLog getLog(SensorType sensorType, int entryCount) throws SQLException
    {
        try
        {
            connect();
            PreparedStatement statement = prepareStatement(
                    "select top(?) sensor.Value, sensor.Created, city.Name as CityName " +
                            "from " + sensorType + " sensor " +
                            "join City on City.Id = sensor.CityId " +
                            "order by sensor.created desc"
                    );
            statement.setInt(1, entryCount);
            ResultSet rs = statement.executeQuery();

            ArrayList<SensorLogEntry> entries = new ArrayList<>();
            while (rs.next())
                entries.add(new SensorLogEntry(rs.getFloat("Value"), rs.getTimestamp("Created"), rs.getString("CityName")));

            return new SensorLog(sensorType, entries);
        }
        finally
        {
            disconnect();
        }
    }

    public List<SensorLog> getAllLogs(int entryCount) throws SQLException
    {
        var logs = new ArrayList<SensorLog>();
        for (var sensorType : SensorType.values())
            logs.add(getLog(sensorType, entryCount));
        return logs;
    }
}
