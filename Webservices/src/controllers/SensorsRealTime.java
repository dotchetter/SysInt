package controllers;
import repositories.SensorType;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/live/temperature")
public class SensorsRealTime extends SensorsRealTimeBase
{
    public SensorsRealTime() throws IOException, ClassNotFoundException
    {

    }

    @OnOpen
    public void onOpen(Session session)
    {
        super.onOpen(session, SensorType.TEMPERATURE);
    }


}
