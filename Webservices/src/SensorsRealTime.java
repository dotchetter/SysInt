import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

@ServerEndpoint("/SensorsRealTime")
public class SensorsRealTime
{
    Session session = null;

    public SensorsRealTime()
    {
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                try
                {
                    sendData();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    @OnOpen
    public synchronized void onOpen(Session session) throws IOException
    {
        this.session = session;
    }

    public synchronized void sendData() throws IOException
    {
        if (session != null)
            session.getBasicRemote().sendText(Instant.now().toString());
    }

    @OnClose
    public synchronized void onClose(Session session)
    {

    }
}
