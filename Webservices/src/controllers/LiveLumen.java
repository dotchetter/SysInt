package controllers;

import repositories.SensorType;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/live/lumen")
public class LiveLumen extends LiveSensor
{
    public LiveLumen() throws IOException, ClassNotFoundException
    {
    }

    @OnOpen
    public void onOpen(Session session)
    {
        super.onOpen(session, SensorType.LUMEN);
    }
}
