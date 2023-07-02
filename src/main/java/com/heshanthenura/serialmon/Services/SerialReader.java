package com.heshanthenura.serialmon.Services;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortIOException;
import com.fazecast.jSerialComm.SerialPortTimeoutException;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.InputStream;

public class SerialReader {

    private SerialPort comPort;
    private Thread readThread;
    private InputStream inputStream;
    private TextArea textArea;

    public void SerialReader(String port, int baudRate, TextArea textArea, TextArea errorField,Button startBtn,Button stopBtn) {
        if (port != null && !port.trim().isEmpty() && baudRate != 0) {
            comPort = SerialPort.getCommPort(port);
            comPort.openPort();
            comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
            comPort.setBaudRate(baudRate);

            this.textArea = textArea;
            inputStream = comPort.getInputStream();

            readThread = new Thread(() -> {
                try {
                    byte[] buffer = new byte[1024];
                    int numBytes;

                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            numBytes = inputStream.read(buffer);
                            if (numBytes > 0) {
                                String data = new String(buffer, 0, numBytes);
                                Platform.runLater(() -> textArea.appendText(data));
                            }
                        } catch (SerialPortTimeoutException e) {
                            // Handle timeout exception
                            // You can print an error message or handle it as needed
                            // Here, we don't print any message when a timeout occurs without any data
                        } catch (SerialPortIOException e) {
                            System.out.println("Port Busy");
                            Platform.runLater(() -> errorField.appendText("Port: " + port + " appears to be busy or used by another app"));
                            startBtn.setDisable(true);
                            stopBtn.setDisable(false);
                            break;
                        } catch (Exception e) {
                            System.out.println("Error Reading Port");
                            startBtn.setDisable(true);
                            stopBtn.setDisable(false);
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error Reading Port");
                    e.printStackTrace();
                } finally {
                    closeResources();
                }
            });

            readThread.start();
        } else {
            if(!(port != null && !port.trim().isEmpty())){
                Platform.runLater(() -> errorField.appendText("Port Seems to be empty or Unavailable"+"\n"));
            }
            if (baudRate == 0){
                Platform.runLater(() -> errorField.appendText("Baud Rate Seems to be empty or Unavailable"+"\n"));
            }
            startBtn.setDisable(true);
            stopBtn.setDisable(false);
        }
    }

    public void StopSerialReader() {
        if (readThread != null && readThread.isAlive()) {
            readThread.interrupt();

            try {
                readThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            closeResources();

            System.out.println("Serial reader stopped");
        } else {
            System.out.println("Serial reader is not running");
        }
    }

    private void closeResources() {
        try {
            if (inputStream != null)
                inputStream.close();

            if (comPort != null && comPort.isOpen())
                comPort.closePort();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
