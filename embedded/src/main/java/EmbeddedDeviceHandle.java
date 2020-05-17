package main.java;
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
                System.out.print((char) in.read());
            }
        } catch (Exception e) {
            e.printStackTrace();
            in.close();
        }
        chosenPort.closePort();
    }

    public static void main(String[] args) {
        EmbeddedDeviceHandle c = new EmbeddedDeviceHandle();
        while (true) {
            try {
                c.readSerial();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}