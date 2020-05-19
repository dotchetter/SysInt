package controllers;

import repositories.SensorType;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/live/humidity")
public class LiveHumidity extends LiveSensor
{
    public LiveHumidity() throws IOException, ClassNotFoundException
    {

    }

    @OnOpen
    public void onOpen(Session session)
    {
        super.onOpen(session, SensorType.HUMIDITY);
    }
}
