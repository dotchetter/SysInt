package main.java;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Communicator {

    public Communicator(){}

    public void readSerial() throws IOException {
        SerialPort ports[] = SerialPort.getCommPorts();
        System.out.println("Select a port: ");
        String input = new String();
        Scanner scanner = new Scanner(System.in);
        int choice;

        int i = 1;
        for(SerialPort port : ports){
            System.out.println(i + "*" + port.getSystemPortName());
            i++;
        }

        choice = scanner.nextInt();
        SerialPort port = ports[choice - 1];
        if (port.openPort()){
            System.out.println("Succesfully opened port: " + port.getSystemPortName());
        } else {
            System.out.println("Could not open port....");
            return;
        }
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        InputStream in = null;

        try {
            in = port.getInputStream();
            while(true) {
                System.out.print((char) in.read());
            }
        } catch (Exception e) {
            e.printStackTrace();
            in.close();
        }
        port.closePort();
    }


    public static void main(String[] args) {
        Communicator c = new Communicator();
        while (true) {
            try {
                c.readSerial();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}