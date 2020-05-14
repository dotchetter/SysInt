package repositories;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

abstract public class SqlDAO
{
    private Properties properties = new Properties();
    private Connection connection;

    public SqlDAO() throws ClassNotFoundException, IOException
    {
        try (FileInputStream stream = new FileInputStream("db.properties"))
        {
            properties.load(stream);
        }
    }

    protected void connect() throws SQLException
    {
        connection = DriverManager.getConnection(
                properties.getProperty("url"));
    }

    protected ResultSet executeQuery(String query) throws SQLException
    {
        Statement s = connection.createStatement();
        return s.executeQuery(query);
    }

    protected PreparedStatement prepareStatement(String query) throws SQLException
    {
        return connection.prepareStatement(query);
    }

    protected void disconnect() throws SQLException
    {
        if (connection != null)
            connection.close();
    }
}

