package controllers;
import com.google.gson.Gson;
import models.SensorLog;
import models.SensorLogEntry;
import repositories.SensorType;
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
    Timer timer = new Timer();

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

    public void sendData() throws IOException, SQLException
    {
        var timestamp = new Timestamp(Instant.now().toEpochMilli());
        List<SensorLog> sensors = new ArrayList<>();
        sensors.add(new SensorLog(SensorType.TEMPERATURE, new SensorLogEntry(new Random().nextFloat()*100, timestamp, "Uppsala")));
        sensors.add(new SensorLog(SensorType.HUMIDITY, new SensorLogEntry(new Random().nextFloat()*100, timestamp, "Uppsala")));
        sensors.add(new SensorLog(SensorType.LUMEN, new SensorLogEntry(new Random().nextFloat()*100, timestamp, "Uppsala")));
        session.getBasicRemote().sendText(new Gson().toJson(sensors));
     }

    @OnClose
    public void onClose(Session session)
    {
        timer.cancel();
    }
}
