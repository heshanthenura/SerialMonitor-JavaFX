package com.heshanthenura.serialmon.Services;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortIOException;
import com.fazecast.jSerialComm.SerialPortTimeoutException;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SerialReader {

    public SerialPort comPort;
    private Thread readThread;
    private InputStream inputStream;
    private TextArea textArea;

    public void SerialReader(String port, int baudRate, TextArea textArea, TextArea errorField, Button startBtn, Button stopBtn) {
        if (port != null && !port.trim().isEmpty() && baudRate != 0) {
            comPort = SerialPort.getCommPort(port);
            comPort.openPort();
            comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
            comPort.setBaudRate(baudRate);
            this.textArea = textArea;
            inputStream = comPort.getInputStream();

            readThread = new Thread(() -> {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String line;
                    while ((line = bufferedReader.readLine()) != null && !Thread.currentThread().isInterrupted()) {
                        try {
                            String finalLine = line;
                            Platform.runLater(() -> {
                                textArea.appendText(finalLine + "\n");
                                try {
                                    new Plotter().plotter(finalLine);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } catch (Exception e) {
                            System.out.println("Error Reading Port");
                            Platform.runLater(() -> {
                                startBtn.setDisable(true);
                                stopBtn.setDisable(false);
                                errorField.appendText(e + "\n");
                            });
                            e.printStackTrace();
                        }
                    }
                } catch (SerialPortTimeoutException e) {
                    // Handle timeout exception
                } catch (SerialPortIOException e) {
                    System.out.println("Port Busy");
                    Platform.runLater(() -> {
                        startBtn.setDisable(true);
                        stopBtn.setDisable(false);
                        errorField.appendText("Port: " + port + " appears to be busy or used by another app\n");
                    });
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Error Reading Port");
                    Platform.runLater(() -> {
                        startBtn.setDisable(true);
                        stopBtn.setDisable(false);
                        errorField.appendText(e + "\n");
                    });
                    e.printStackTrace();
                } finally {
                    closeResources();
                }
            });

            readThread.start();
        } else {
            if (!(port != null && !port.trim().isEmpty())) {
                Platform.runLater(() -> errorField.appendText("Port Seems to be empty or Unavailable\n"));
            }
            if (baudRate == 0) {
                Platform.runLater(() -> errorField.appendText("Baud Rate Seems to be empty or Unavailable\n"));
            }
            startBtn.setDisable(true);
            stopBtn.setDisable(false);
        }
    }

    public void StopSerialReader() {
        try {
            Thread thread = new Thread(() -> {
                try {
                    if (readThread != null && readThread.isAlive()) {
                        readThread.interrupt();
                        comPort.closePort();
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
                } catch (Exception e) {
                    System.out.println("Error Stopping");
                }
            });
            thread.start();
        } catch (Exception e) {
            System.out.println("Error Stopping");
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
