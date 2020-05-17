package repositories;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class EmbeddedDeviceHandle implements Runnable {

    public EmbeddedDeviceHandle(){}

    public void run() {

        /*
        * Act as a handle between local embedded device equipped with sensors,
        * and provide callers / instantiators of this class with data from the
        * device. */

        InputStream in = null;
        SerialPort chosenPort = null;
        SerialPort ports[] = SerialPort.getCommPorts();

        for (SerialPort p : ports)  {
            if (p.openPort()){
                System.out.println("Opened port: " + p.getSystemPortName());
                chosenPort = p;
            }
        }

        if (chosenPort == null) {
            System.out.println("No available ports, exiting . . .");
            return;
        }

        chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        while (true) {
            try {
                in = chosenPort.getInputStream();
                while(true) {
                    System.out.print((char) in.read());
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    in.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                chosenPort.closePort();
            }
        }
    }
}