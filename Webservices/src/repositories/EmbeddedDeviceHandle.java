package repositories;
import com.fazecast.jSerialComm.SerialPort;
import models.SensorLogEntry;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Scanner;

public class EmbeddedDeviceHandle implements Runnable {
    String deviceLocation;

    public EmbeddedDeviceHandle(String deviceLocation){
        this.deviceLocation = deviceLocation;
    }

    public void run() {

        SerialPort ports[] = SerialPort.getCommPorts();
        String input = new String();
        Scanner scanner = new Scanner(System.in);
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
        InputStream in = null;

        try {
            in = chosenPort.getInputStream();
            while(true) {
                inBuf += (char)in.read();
                inBuf.strip().replace(" ", "").replace("\n","");
                if (!inBuf.contains("<") && !inBuf.contains(">")) {
                    continue;
                }

                splitMsgbuf = inBuf.replace("<", "")
                        .replace(">", "")
                        .strip().split(";");

                for (SensorType iterativeSensorType : SensorType.values()) {
                    try {
                        Float temp = Float.valueOf(splitMsgbuf[0]);
                        Float humid = Float.valueOf(splitMsgbuf[1]);
                        Float light = Float.valueOf(splitMsgbuf[2]);

                        Timestamp time = new Timestamp(Instant.now().toEpochMilli());
                        SensorLogEntry tempEntry = new SensorLogEntry(temp, time, this.deviceLocation, SensorType.TEMPERATURE);
                        SensorLogEntry humidEntry = new SensorLogEntry(humid, time, this.deviceLocation, SensorType.HUMIDITY);
                        SensorLogEntry lightEntry = new SensorLogEntry(light, time, this.deviceLocation, SensorType.LUMEN);

                        StaticDeviceMessageQueue.enqueue(tempEntry);
                        StaticDeviceMessageQueue.enqueue(humidEntry);
                        StaticDeviceMessageQueue.enqueue(lightEntry);
                    } catch (Exception e) {
                        continue;
                    }
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