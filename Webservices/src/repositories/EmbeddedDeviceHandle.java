package repositories;
import com.fazecast.jSerialComm.SerialPort;
import models.SensorLogEntry;
import models.SensorLog;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class EmbeddedDeviceHandle implements Runnable {
    private String deviceLocation;
    private Map<SensorType, SensorLogEntryQueue> queues = new HashMap<>();;

    public EmbeddedDeviceHandle(String deviceLocation){
        this.deviceLocation = deviceLocation;
        for (var sensorType : SensorType.values())
            queues.put(sensorType, new SensorLogEntryQueue());
    }

    public void run() {

        SerialPort ports[] = SerialPort.getCommPorts();
        String inBuf = new String();
        String[] splitMsgbuf;

        SerialPort chosenPort = null;

        for (SerialPort p : ports)  {
            if (p.openPort()){
                System.out.print("EmbeddedDeviceHandle: Succesfully opened port: " + p.getSystemPortName() + ". ");
                System.out.println("Reading messages from device. Location: " + this.deviceLocation);
                chosenPort = p;
            }
        }

        if (chosenPort == null) {
            System.out.println("No serial device discovered. Make sure all other access terminals are closed.");
            return;
        }

        chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        chosenPort.setBaudRate(9600);
        InputStream in = null;

        try {
            in = chosenPort.getInputStream();
            while(true) {
                inBuf += (char)in.read();
                inBuf.strip().replace(" ", "").replace("\n","");
                if (!inBuf.contains("<") || !inBuf.contains(">")) {
                    continue;
                }
                System.out.println(inBuf);
                splitMsgbuf = inBuf.replace("<", "")
                        .replace(">", "")
                        .strip().split(";");

                try {
                    Float temp = Float.valueOf(splitMsgbuf[0]);
                    Float humid = Float.valueOf(splitMsgbuf[1]);
                    Float light = Float.valueOf(splitMsgbuf[2]);

                    Timestamp time = new Timestamp(Instant.now().toEpochMilli());
                    SensorLogEntry tempEntry = new SensorLogEntry(temp, time, this.deviceLocation);
                    SensorLogEntry humidEntry = new SensorLogEntry(humid, time, this.deviceLocation);
                    SensorLogEntry lightEntry = new SensorLogEntry(light, time, this.deviceLocation);

                    enqueue(tempEntry, humidEntry, lightEntry);

                } catch (Exception e) {
                    System.err.println("Could not enqueue log entries from parsed message: " + inBuf);
                    inBuf = "";
                    continue;
                }
                inBuf = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                in.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        chosenPort.closePort();
    }

    private synchronized void enqueue(SensorLogEntry tempEntry, SensorLogEntry humidEntry, SensorLogEntry lightEntry)
    {
        queues.get(SensorType.TEMPERATURE).enqueue(tempEntry);
        queues.get(SensorType.HUMIDITY).enqueue(humidEntry);
        queues.get(SensorType.LUMEN).enqueue(lightEntry);
    }

    public synchronized SensorLogEntry dequeue(SensorType sensorType)
    {
        return queues.get(sensorType).dequeue();
    }
}
