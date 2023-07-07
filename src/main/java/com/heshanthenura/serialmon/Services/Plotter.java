package com.heshanthenura.serialmon.Services;

import com.heshanthenura.serialmon.MainController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.time.Duration;
import java.time.LocalTime;


public class Plotter {

    public static Boolean plotStatus = false;

    public static LocalTime startTime;
    private static LineChart<?, ?> chart;
    private static XYChart.Series series;

    public static String PlotVariable;

    public void plotter(String data) throws InterruptedException {
        String variable = data.trim().split(":")[0].trim();
        System.out.println(PlotVariable.equals(variable));
        if (plotStatus==true){
                LocalTime currentTime = LocalTime.now();
                String interval = String.valueOf(Duration.between(startTime, currentTime)).replaceAll("P", "").replaceAll("T", "").replaceAll("S", "");
                System.out.println("Start Time: " + startTime + "Current Time:" + currentTime + ": Interval :" + interval);
                String number = data.trim().split(":")[1].trim();

                Platform.runLater(() -> {
                    if (series == null) {
                        series = new XYChart.Series(); // Create a new series
                        chart.getData().add(series); // Add the series to the chart
                    }
                    series.getData().add(new XYChart.Data(interval, Long.parseLong(number))); // Add a new data point to the series
//            if (series.getData().size() > 20) {
//                series.getData().remove(0);
//            }
                });

        }

    }

    public static void SetupPlotter(LineChart<?, ?> linePlotter) {
        chart = linePlotter;
    }

    public static void SetupTime() {
        startTime = LocalTime.now();
    }
}
