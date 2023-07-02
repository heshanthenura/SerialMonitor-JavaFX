package com.heshanthenura.serialmon.Services;

import com.fazecast.jSerialComm.SerialPort;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Functions {
    public void SerialReader(String port, int baudRate, TextArea textArea, TextArea errorField) {

        // Replace "COM3" with your COM port name
        SerialPort comPort = SerialPort.getCommPort("COM3");
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        // Change the baud rate here
        comPort.setBaudRate(115200); // Replace with your desired baud rate

        // Create a separate thread to continuously read from the serial port
        Thread readThread = new Thread(() -> {
            try {
                InputStream inputStream = comPort.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println("Received data: " + line);
//                        textArea.appendText(line+"\n");
                }

                System.out.println("Done");
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        readThread.start();
    }

    public void SerialReader2(String port, int baudRate, TextArea textArea, TextArea errorField) {

        SerialPort comPort = SerialPort.getCommPort(port);
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        comPort.setBaudRate(baudRate);

        Thread readThread = new Thread(() -> {
            try {
                InputStream inputStream = comPort.getInputStream();

                while (true) {
                    int data = inputStream.read();
                    if (data == -1) {
                        // End of stream reached
                        break;
                    }
                    System.out.print((char) data);
                    // textArea.appendText(String.valueOf((char) data));
                }

                System.out.println("Done");
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        readThread.start();
    }


    public static void main(String[] args) {
        new Functions().SerialReader2("COM3",115200,null,null);
    }
}
