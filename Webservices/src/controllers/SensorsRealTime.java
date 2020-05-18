package controllers;
import com.google.gson.Gson;
import repositories.EmbeddedDeviceHandle;
import repositories.SensorDAO;
import repositories.StaticDeviceMessageQueue;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@ServerEndpoint("/SensorsRealTime")
public class SensorsRealTime
{
    SensorDAO sensorDao = new SensorDAO();
    Session session = null;
    Timer timer = new Timer();
    private static Thread threadForDeviceHandle = null;
    private static EmbeddedDeviceHandle deviceHandle = new EmbeddedDeviceHandle("Stockholm");

    public SensorsRealTime() throws IOException, ClassNotFoundException
    {
    }

    @OnOpen
    public void onOpen(Session session)
    {
        this.session = session;

        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                try
                {
                    sendData();
                } catch (IOException | SQLException e)
                {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    public synchronized void sendData() throws IOException, SQLException
    {
        if (deviceHandle == null) {
            deviceHandle = new EmbeddedDeviceHandle("Stockholm");
        }

        if (threadForDeviceHandle == null) {
            threadForDeviceHandle = new Thread(deviceHandle);
            threadForDeviceHandle.start();
        }

        var sensorLog = StaticDeviceMessageQueue.dequeue();
        if (sensorLog != null)
        {
            session.getBasicRemote().sendText(new Gson().toJson(sensorLog));
            sensorDao.createLogEntries(sensorLog);
        }
    }

    @OnClose
    public void onClose(Session session)
    {
        timer.cancel();
    }
}
