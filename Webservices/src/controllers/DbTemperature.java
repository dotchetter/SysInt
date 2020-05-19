package controllers;

import repositories.SensorType;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DbTemperature", urlPatterns = "/db/temperature")
public class DbTemperature extends DbSensor
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        super.doGet(SensorType.TEMPERATURE, request, response);
    }
}

