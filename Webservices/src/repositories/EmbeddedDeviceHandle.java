package repositories;
import com.fazecast.jSerialComm.SerialPort;
import models.SensorLogEntry;
import models.SensorLog;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;

public class EmbeddedDeviceHandle implements Runnable {
    String deviceLocation;

    public EmbeddedDeviceHandle(String deviceLocation){
        this.deviceLocation = deviceLocation;
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
                        SensorLog tempEntry = new SensorLog(SensorType.TEMPERATURE, new SensorLogEntry(temp, time, this.deviceLocation));
                        SensorLog humidEntry = new SensorLog(SensorType.HUMIDITY, new SensorLogEntry(humid, time, this.deviceLocation));
                        SensorLog lightEntry = new SensorLog(SensorType.LUMEN, new SensorLogEntry(light, time, this.deviceLocation));

                    StaticDeviceMessageQueue.enqueue(tempEntry);
                    StaticDeviceMessageQueue.enqueue(humidEntry);
                    StaticDeviceMessageQueue.enqueue(lightEntry);
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
}
