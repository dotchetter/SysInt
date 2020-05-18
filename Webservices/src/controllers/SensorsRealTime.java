package controllers;
import com.google.gson.Gson;
import models.SensorLogEntry;
import repositories.EmbeddedDeviceHandle;
import repositories.SensorDAO;
import repositories.SensorType;
import repositories.StaticDeviceMessageQueue;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@ServerEndpoint("/SensorsRealTime")
public class SensorsRealTime
{
    Session session = null;
    SensorDAO sensorDAO = new SensorDAO();
    Timer timer = new Timer();
    private static Thread threadForDeviceHandle = null;
    private static EmbeddedDeviceHandle deviceHandle = new EmbeddedDeviceHandle("Stockholm");
    private static SensorLogEntry latestEntry = null;

    public SensorsRealTime() throws IOException
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

        var entry = StaticDeviceMessageQueue.dequeue();

        if (latestEntry == null) {
            latestEntry = StaticDeviceMessageQueue.dequeue();
            session.getBasicRemote().sendText(new Gson().toJson("No messages received from sensor since start."));
        }

        if (session != null && entry != null) {
            var timestamp = new Timestamp(Instant.now().toEpochMilli());
            List<SensorLogEntry> sensorList = new ArrayList<>();
            sensorList.add(latestEntry);
            SensorLogEntry newLogEntry = StaticDeviceMessageQueue.dequeue();

            if (newLogEntry != null) {
                latestEntry = newLogEntry;
            }

            session.getBasicRemote().sendText(new Gson().toJson(sensorList));
        }
    }

    @OnClose
    public void onClose(Session session)
    {
        timer.cancel();
    }
}
