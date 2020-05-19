package controllers;

import repositories.SensorType;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DbHumidity", urlPatterns = "/db/humidity")
public class DbHumidity extends DbSensor
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        super.doGet(SensorType.HUMIDITY, request, response);
    }
}

