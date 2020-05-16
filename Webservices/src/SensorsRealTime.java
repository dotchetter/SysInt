//import models.SensorLogEntry;
//import repositories.SensorDAO;
//import repositories.SensorType;
import models.SensorLogEntry;
import repositories.SensorDAO;
import repositories.SensorType;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

@ServerEndpoint("/SensorsRealTime")
public class SensorsRealTime
{
    Session session = null;
    SensorDAO sensorDAO = new SensorDAO();

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
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    public void sendData() throws IOException, SQLException
    {
        if (session != null)
        {
            SensorLogEntry currentHumidity = sensorDAO.getLogEntries(SensorType.HUMIDITY, 1).get(0);
            session.getBasicRemote().sendText(Instant.now().toString() + "\nHumidity" + currentHumidity.getValue());
        }
    }

    @OnClose
    public void onClose(Session session)
    {

    }
}
