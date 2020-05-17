package repositories;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class EmbeddedDeviceHandle implements Runnable {

    public EmbeddedDeviceHandle(){}

    public void run() {

        SerialPort ports[] = SerialPort.getCommPorts();
        String input = new String();
        Scanner scanner = new Scanner(System.in);
        String inBuf = new String();

        SerialPort chosenPort = null;

        for (SerialPort p : ports)  {
            if (p.openPort()){
                System.out.println("Succesfully opened port: " + p.getSystemPortName());
                chosenPort = p;
            }
        }

        if (chosenPort == null) {
            System.out.println("No available ports, exiting . . .");
            return;
        }

        chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        InputStream in = null;

        try {
            in = chosenPort.getInputStream();
            while(true) {
                inBuf += (char)in.read();
                if (inBuf.contains(">")) {
                    System.out.println(inBuf);
                    inBuf = "";
                }
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