package com.heshanthenura.serialmon;

import com.heshanthenura.serialmon.Services.Plotter;
import com.heshanthenura.serialmon.Services.PortList;
import com.heshanthenura.serialmon.Services.SerialReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import static com.heshanthenura.serialmon.Services.Plotter.PlotVariable;
import static com.heshanthenura.serialmon.Services.Plotter.plotStatus;

public class MainController implements Initializable {
    String selectedPort = null;

    int baudRate=0;

    @FXML
    private Button StartPlotBtn;

    @FXML
    private Button StopPlotBtn;


    @FXML
    private ChoiceBox<String> PortListBtn;
//    private ChoiceBox<String> PortListBtn;
//    <ChoiceBox fx:id="PortListBtn" onAction="#portSelected" prefWidth="150.0" />

    @FXML
    private TextField baudRateInp;

    @FXML
    private Button baudRateSetBtn;

    @FXML
    private TextField manualPort;

    @FXML
    private Button manualPortSetBtn;

    @FXML
    private Button scanBtn;

    @FXML
    private Button startBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private TextArea textArea;

    @FXML
    private TextArea errorField;


    @FXML
    public LineChart<?, ?> chart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Button addBtn;

    @FXML
    private TextField variableInput;

    SerialReader serialReader = new SerialReader();

    public void portSelected(ActionEvent actionEvent) {
        try{
            System.out.println("Port Selected "+PortListBtn.getValue());

            if (!(PortListBtn.getValue().equals("No Ports Available"))){
                String[] parts = PortListBtn.getValue().split("-");
                if (parts.length > 1) {
                    String contentAfterHyphen = parts[1].trim();
                    System.out.println(contentAfterHyphen);
                    selectedPort=contentAfterHyphen;
                }
            }else {
                errorField.appendText("No Ports Available"+"\n");
                System.out.println("No Ports Available");
            }

        }catch (Exception e){
            throw e;
        }

    }

    @FXML
    void scanPorts(MouseEvent event) {

        new PortList().PortList(PortListBtn);

    }

    @FXML
    void setBaudRate(MouseEvent event) {

        String rate = baudRateInp.getText();
        if (rate != null && !rate.trim().isEmpty()) {
            baudRate = Integer.parseInt(rate);
        }
        else {
            errorField.appendText("Please Enter BaudRate"+"\n");
        }

    }

    @FXML
    void setManualPort(MouseEvent event) {

        String port = manualPort.getText();
        if (port != null && !port.trim().isEmpty()) {
            selectedPort = manualPort.getText();
                    }
        else {
            errorField.appendText("Please Enter Port Name"+"\n");
        }

    }

    @FXML
    void start(MouseEvent event) {

        System.out.println("Selected Port: "+selectedPort);
        System.out.println("Selected Baud Rate: "+baudRate);
        System.out.println("Thread Starting");
        serialReader.SerialReader(selectedPort,baudRate,textArea,errorField,startBtn,stopBtn);
        startBtn.setDisable(true);
        stopBtn.setDisable(false);

    }

    @FXML
    void stop(MouseEvent event) {
        startBtn.setDisable(false);
        stopBtn.setDisable(true);
        serialReader.StopSerialReader();

    }

    @FXML
    void startPlot(MouseEvent event) {
        plotStatus=true;
        StartPlotBtn.setDisable(true);
        StopPlotBtn.setDisable(false);
    }

    @FXML
    void stopPlot(MouseEvent event) {
        plotStatus=false;
        StartPlotBtn.setDisable(false);
        StopPlotBtn.setDisable(true);
    }

    @FXML
    void addVariable(MouseEvent event) {
        PlotVariable=variableInput.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //listing ports at startup
        new PortList().PortList(PortListBtn);

        // make baud rate input to only input numbers
        baudRateInp.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                baudRateInp.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        stopBtn.setDisable(true);
        StopPlotBtn.setDisable(true);

//        XYChart.Series series = new XYChart.Series();
//        series.getData().add(new XYChart.Data("1.5",23));
//        series.getData().add(new XYChart.Data("2.2",30));
//        series.getData().add(new XYChart.Data("3",32));
//        series.getData().add(new XYChart.Data("4",36));
//        series.getData().add(new XYChart.Data("5",40));
//        chart.getData().addAll(series);

        Plotter.SetupPlotter(chart);
        Plotter.SetupTime();
        chart.setStyle("-fx-stroke-width: 1px;");
    }

}