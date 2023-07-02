module com.heshanthenura.serialmon {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;


    opens com.heshanthenura.serialmon to javafx.fxml;
    exports com.heshanthenura.serialmon;
}