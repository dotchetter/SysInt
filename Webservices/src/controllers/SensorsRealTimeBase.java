package controllers;

import com.google.gson.Gson;
import repositories.EmbeddedDeviceHandle;
import repositories.SensorDAO;
import repositories.SensorType;

import javax.websocket.OnClose;
import javax.websocket.Session;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class SensorsRealTimeBase
{
    protected SensorDAO sensorDao = new SensorDAO();
    protected Session session = null;
    protected Timer timer = new Timer();
    protected static Thread threadForDeviceHandle = null;
    protected static EmbeddedDeviceHandle deviceHandle = null;
    protected static Properties properties = null;

    protected SensorsRealTimeBase() throws IOException, ClassNotFoundException
    {
    }

    protected synchronized void onOpen(Session session, SensorType sensorType)
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
        if (deviceHandle == null)
        {
            deviceHandle = new EmbeddedDeviceHandle(properties.getProperty("city"));
        }

        if (threadForDeviceHandle == null)
        {
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
                    sendData(sensorType);
                } catch (IOException | SQLException e)
                {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    public synchronized void sendData(SensorType sensorType) throws IOException, SQLException
    {
        var sensorLogEntry = deviceHandle.dequeue(sensorType);
        if (sensorLogEntry != null)
        {
            //Send to ws endpoint of sub class
            session.getBasicRemote().sendText(new Gson().toJson(sensorLogEntry));

            //Send to database
            sensorDao.createLogEntry(sensorType, sensorLogEntry);
        }
    }

    @OnClose
    public void onClose()
    {
        timer.cancel();
    }
}
