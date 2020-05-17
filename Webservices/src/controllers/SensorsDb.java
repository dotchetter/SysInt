package controllers;

import com.google.gson.Gson;
import models.SensorLog;
import repositories.SensorDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "SensorsDb", urlPatterns = "/SensorsDb")
public class SensorsDb extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        SensorDAO sensorDao = null;
        try
        {
            sensorDao = new SensorDAO();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return;
        }
        setAccessControlHeaders(response);
        response.setContentType("application/json");
        var writer = response.getWriter();
        List<SensorLog> sensorLogs = null;
        try
        {
            sensorLogs = sensorDao.getAllLogs(1000);
        } catch (SQLException e)
        {
            e.printStackTrace();
            return;
        }

        writer.print(new Gson().toJson(sensorLogs));
        writer.flush();
    }

    //for Preflight
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
    {
        setAccessControlHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void setAccessControlHeaders(HttpServletResponse resp)
    {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET");
    }
}
