package controllers;

import repositories.SensorType;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DbLumen", urlPatterns = "/db/lumen")
public class DbLumen extends DbSensor
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        super.doGet(SensorType.LUMEN, request, response);
    }
}

