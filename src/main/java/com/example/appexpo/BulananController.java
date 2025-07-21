package com.example.appexpo;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.temporal.WeekFields;
import java.util.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.util.Objects;

public class BulananController implements Initializable {
    @FXML private LineChart<String, Number> diagramBulanan;
    @FXML private CategoryAxis xAxis;
    @FXML private Label targetUtama, targetTercapai, persenCapai;
    @FXML private ProgressBar progressBulanan;
    @FXML private AnchorPane rootPane;
    @FXML private Button pindahStage, pindahkeTugas;

    private ManajerCSV csvManager = new ManajerCSV();
    private int currentMonth = LocalDate.now().getMonthValue();
    private int currentYear = LocalDate.now().getYear();

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        rootPane.setTranslateY(50);
        rootPane.setOpacity(0);
        rootPane.setScaleX(0.95);
        rootPane.setScaleY(0.95);

        TranslateTransition slide = new TranslateTransition(Duration.millis(600), rootPane);
        slide.setFromY(50);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.EASE_BOTH);

        FadeTransition fade = new FadeTransition(Duration.millis(600), rootPane);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(600), rootPane);
        scale.setFromX(0.95);
        scale.setFromY(0.95);
        scale.setToX(1);
        scale.setToY(1);
        scale.setInterpolator(Interpolator.EASE_OUT);

        new ParallelTransition(rootPane, slide, fade, scale).play();


        updateChartForCurrentMonth();

        List<ManajerCSV.Tugas> semuaTugas = csvManager.bacaSemuaTugas();
        int bulanIni = LocalDate.now().getMonthValue();
        int tahunIni = LocalDate.now().getYear();

        long totalTarget = semuaTugas.stream()
                .filter(tugas -> {
                    try {
                        LocalDate tanggal = LocalDate.parse(tugas.getTanggal());
                        return tanggal.getMonthValue() == bulanIni &&
                                tanggal.getYear() == tahunIni;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count();

        long totalTercapai = semuaTugas.stream()
                .filter(tugas -> {
                    try {
                        LocalDate tanggal = LocalDate.parse(tugas.getTanggal());
                        return tanggal.getMonthValue() == bulanIni &&
                                tanggal.getYear() == tahunIni &&
                                tugas.isSelesai();
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count();

        double persentase = totalTarget == 0 ? 0 : (double) totalTercapai / totalTarget;

        targetUtama.setText(String.valueOf(totalTarget));
        targetTercapai.setText(String.valueOf(totalTercapai));
        progressBulanan.setProgress(persentase);
        animateProgressBar(persentase);
        progressBulanan.progressProperty().addListener((unused, oldval, newVal) -> animatePercentageText(newVal.doubleValue()));
    }

    private void updateChartForCurrentMonth() {
        List<ManajerCSV.Tugas> semuaTugas = csvManager.bacaSemuaTugas();
        Map<Integer, WeeklyData> weeklyData = calculateWeeklyData(semuaTugas);
        setupMonthlyLineChart(weeklyData);
    }

    private Map<Integer, WeeklyData> calculateWeeklyData(List<ManajerCSV.Tugas> semuaTugas) {
        Map<Integer, WeeklyData> weeklyData = new TreeMap<>();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        for (int week = 1; week <= 4; week++) {
            weeklyData.put(week, new WeeklyData());
        }

        for (ManajerCSV.Tugas tugas : semuaTugas) {
            try {
                LocalDate tanggal = LocalDate.parse(tugas.getTanggal());
                if (tanggal.getMonthValue() == currentMonth && tanggal.getYear() == currentYear) {
                    int weekOfMonth = tanggal.get(weekFields.weekOfMonth());
                    weekOfMonth = Math.min(weekOfMonth, 4);

                    WeeklyData data = weeklyData.get(weekOfMonth);
                    data.totalTasks++;
                    if (tugas.isSelesai()) {
                        data.completedTasks++;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error parsing tanggal: " + e.getMessage());
            }
        }
        return weeklyData;
    }

    private void setupMonthlyLineChart(Map<Integer, WeeklyData> weeklyData) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(Month.of(currentMonth).toString() + " " + currentYear);

        List<String> weekLabels = new ArrayList<>();

        for (int week = 1; week <= 4; week++) {
            WeeklyData data = weeklyData.get(week);
            double percentage = data.totalTasks == 0 ? 0 :
                    (double) data.completedTasks / data.totalTasks * 100;

            series.getData().add(new XYChart.Data<>("Minggu " + week, percentage));
            weekLabels.add("Minggu " + week);
        }

        xAxis.setCategories(FXCollections.observableArrayList(weekLabels));

        NumberAxis yAxis = (NumberAxis) diagramBulanan.getYAxis();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);
        yAxis.setTickUnit(10);

        diagramBulanan.getData().clear();
        diagramBulanan.getData().add(series);

        Platform.runLater(() -> {
            Node lineNode = series.getNode();
            if (lineNode != null) {
                lineNode.setVisible(false);
            }
            animatePointsAndLine(series);
        });
    }
    private void animateProgressBar(double targetProgress) {
        progressBulanan.setProgress(0);
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(progressBulanan.progressProperty(), targetProgress,
                                Interpolator.EASE_BOTH)
                )
        );
        timeline.play();
    }

    private void animatePercentageText(double targetPercentage) {
        int targetValue = (int) (targetPercentage * 100);
        String targetText = String.format("%02d%%", targetValue);

        Timeline timeline = new Timeline();

        for (int i = 0; i < targetText.length(); i++) {
            final int charIndex = i;
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(200 * i),
                            event -> {
                                String current = persenCapai.getText();
                                if (current.length() <= charIndex) current = "  %";
                                char newChar = targetText.charAt(charIndex);
                                String newText = current.substring(0, charIndex)
                                        + newChar
                                        + current.substring(charIndex + 1);
                                persenCapai.setText(newText);
                            }
                    ));
        }

        Timeline randomize = new Timeline(
                new KeyFrame(Duration.millis(50), event -> {
                    int angkaAcak = (int)(Math.random() * 100);
                    String acakText = String.format("%02d%%", angkaAcak);
                    persenCapai.setText(acakText);
                })
        );

        randomize.setCycleCount(8);
        new SequentialTransition(randomize, timeline).play();
    }

    private void animatePointsAndLine(XYChart.Series<String, Number> series) {
        ObservableList<XYChart.Data<String, Number>> dataList = series.getData();
        SequentialTransition sequential = new SequentialTransition();

        for (XYChart.Data<String, Number> data : dataList) {
            Node node = data.getNode();
            if (node != null) {
                node.setOpacity(0);
            }
        }

        for (int i = 0; i < dataList.size(); i++) {
            XYChart.Data<String, Number> data = dataList.get(i);
            Node node = data.getNode();

            if (node != null) {
                FadeTransition fade = new FadeTransition(Duration.millis(300), node);
                fade.setFromValue(0);
                fade.setToValue(1);
                fade.setInterpolator(Interpolator.EASE_OUT);
                fade.setDelay(Duration.millis(i * 25));
                sequential.getChildren().add(fade);
            }
        }

        sequential.setOnFinished(event -> animateLineReveal(series));
        sequential.play();
    }

    private void animateLineReveal(XYChart.Series<String, Number> series) {
        Node lineNode = series.getNode();
        Rectangle clip = new Rectangle(0, 0, 0, diagramBulanan.getHeight());
        lineNode.setVisible(true);
        lineNode.setClip(clip);

        Platform.runLater(() -> {
            Timeline revealLine = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(clip.widthProperty(), 0)),
                    new KeyFrame(Duration.seconds(2), new KeyValue(clip.widthProperty(),
                            diagramBulanan.getWidth(), Interpolator.EASE_OUT))
            );
            revealLine.play();
        });
    }

    public void switchScene(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("statistik-mingguan.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void switchSceneTugas (javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("MainMenuView.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
    private static class WeeklyData {
        int totalTasks = 0;
        int completedTasks = 0;
    }
}