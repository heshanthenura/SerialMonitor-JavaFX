package com.heshanthenura.serialmon;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    private MainController mainController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle("Serial Monitor");
        stage.setScene(scene);

        mainController = fxmlLoader.getController();

        stage.setOnCloseRequest(event -> {
            mainController.serialReader.StopSerialReader();
            // You can also add additional cleanup code here if needed
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
