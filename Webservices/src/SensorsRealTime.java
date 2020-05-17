//import models.SensorLogEntry;
//import repositories.SensorDAO;
//import repositories.SensorType;
import com.google.gson.Gson;
import models.SensorLogEntry;
import repositories.SensorDAO;
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
    SensorDAO sensorDAO = new SensorDAO();
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
        if (session != null)
        {
            var timestamp = new Timestamp(Instant.now().toEpochMilli());
            List<SensorLogEntry> sensorList = new ArrayList<>();
            sensorList.add(new SensorLogEntry(new Random().nextFloat()*100, timestamp, "Uppsala", SensorType.TEMPERATURE));
            sensorList.add(new SensorLogEntry(new Random().nextFloat()*100, timestamp, "Uppsala", SensorType.HUMIDITY));
            sensorList.add(new SensorLogEntry(new Random().nextFloat()*100, timestamp, "Uppsala", SensorType.LUMEN));
            session.getBasicRemote().sendText(new Gson().toJson(sensorList));
        }
    }

    @OnClose
    public void onClose(Session session)
    {
        timer.cancel();
    }
}
