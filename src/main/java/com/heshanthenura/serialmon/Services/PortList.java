package com.heshanthenura.serialmon.Services;

import com.fazecast.jSerialComm.SerialPort;
import javafx.scene.control.ChoiceBox;

public class PortList {

    public void PortList(ChoiceBox PortListBtn){
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println(ports.length);
        if (ports.length == 0){
            PortListBtn.getItems().clear();
            PortListBtn.getItems().add("No Ports Available");
            PortListBtn.setValue("No Ports Available");
        }else {
            PortListBtn.getItems().clear();
            PortListBtn.setValue("Select a Port");
            for (SerialPort port:
                    ports) {
                PortListBtn.getItems().add(port+"-"+port.getSystemPortName());
            }
        }

    }

    public static void main(String[] args) {
        // Get the available serial ports
        SerialPort[] ports = SerialPort.getCommPorts();

        // Print the port names
        System.out.println("Available Ports:");
        for (SerialPort port : ports) {
            System.out.println(port.getSystemPortName());
        }
    }

}
