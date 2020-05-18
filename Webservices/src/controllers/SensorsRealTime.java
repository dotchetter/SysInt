package controllers;
import com.google.gson.Gson;
import repositories.EmbeddedDeviceHandle;
import repositories.SensorDAO;
import repositories.StaticDeviceMessageQueue;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private static EmbeddedDeviceHandle deviceHandle = null;
    private static Properties properties = null;

    public SensorsRealTime() throws IOException, ClassNotFoundException
    {

    }

    @OnOpen
    public synchronized void onOpen(Session session)
    {
        if (properties == null)
        {
            try (var stream = new FileInputStream("db.properties"))
            {
                properties = new Properties();
                properties.load(stream);
            } catch (IOException e)
            {
                e.printStackTrace();
                return;
            }
        }
        if (deviceHandle == null) {
            deviceHandle = new EmbeddedDeviceHandle(properties.getProperty("city"));
        }

        if (threadForDeviceHandle == null) {
            threadForDeviceHandle = new Thread(deviceHandle);
            threadForDeviceHandle.start();
        }

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
