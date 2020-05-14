package repositories;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException
    {
	    // write your test code here
        SensorDAO dao = new SensorDAO();
        try
        {
            dao.connect();
        }
        finally
        {
            dao.disconnect();
        }
    }
}
