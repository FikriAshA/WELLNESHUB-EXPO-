package com.example.appexpo;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class MingguanController implements Initializable {
    @FXML private LineChart<String, Number> diagramHarian;
    @FXML private CategoryAxis xAxis;
    @FXML private Label target, targetTercapai, persen;
    @FXML private ProgressBar persentase;
    @FXML private Button pindahStage, pindahkeTambahTugas;
    @FXML private Pane rootPane;

    private static final String[] namaHari = {"Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu", "Minggu"};

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


        ManajerCSV csvManager = new ManajerCSV();
        List<ManajerCSV.Tugas> semuaTugas = csvManager.bacaSemuaTugas();

        LocalDate sekarang = LocalDate.now();
        LocalDate awalMinggu = sekarang.with(DayOfWeek.MONDAY);
        LocalDate akhirMinggu = sekarang.with(DayOfWeek.SUNDAY);

        long totalTarget = semuaTugas.stream()
                .filter(tugas -> {
                    try {
                        LocalDate tanggal = LocalDate.parse(tugas.getTanggal());
                        return !tanggal.isBefore(awalMinggu) && !tanggal.isAfter(akhirMinggu);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count();

        long totalTercapai = semuaTugas.stream()
                .filter(tugas -> {
                    try {
                        LocalDate tanggal = LocalDate.parse(tugas.getTanggal());
                        return !tanggal.isBefore(awalMinggu) &&
                                !tanggal.isAfter(akhirMinggu) &&
                                tugas.isSelesai();
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count();

        double persentaseSelesai = totalTarget == 0 ? 0 : (double) totalTercapai / totalTarget * 100;

        setupLineChart(semuaTugas);

        target.setText(String.valueOf(totalTarget));
        targetTercapai.setText(String.valueOf(totalTercapai));
        persen.setText("00%");

        animateProgressBar(persentaseSelesai / 100);
        animatePercentageText(persentaseSelesai);
    }

    private void setupLineChart(List<ManajerCSV.Tugas> semuaTugas) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Persentase Tugas Selesai");

        Map<DayOfWeek, Double> persentasePerHari = hitungPersentaseHarian(semuaTugas);

        for (int i = 0; i < namaHari.length; i++) {
            DayOfWeek day = DayOfWeek.of((i + 1) % 7 + 1);
            double persen = persentasePerHari.getOrDefault(day, 0.0);
            series.getData().add(new XYChart.Data<>(namaHari[i], persen));
        }

        xAxis.setCategories(FXCollections.observableArrayList(namaHari));

        NumberAxis yAxis = (NumberAxis) diagramHarian.getYAxis();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);
        yAxis.setTickUnit(10);

        diagramHarian.getData().add(series);

        Platform.runLater(() -> {
            Node lineNode = series.getNode();
            if (lineNode != null) {
                lineNode.setVisible(false);
            }
            animatePointsAndLine(series);
        });
    }

    private Map<DayOfWeek, Double> hitungPersentaseHarian(List<ManajerCSV.Tugas> semuaTugas) {
        Map<DayOfWeek, Integer> totalTugas = new EnumMap<>(DayOfWeek.class);
        Map<DayOfWeek, Integer> tugasSelesai = new EnumMap<>(DayOfWeek.class);

        for (DayOfWeek day : DayOfWeek.values()) {
            totalTugas.put(day, 0);
            tugasSelesai.put(day, 0);
        }

        LocalDate sekarang = LocalDate.now();
        LocalDate awalMinggu = sekarang.with(DayOfWeek.MONDAY);
        LocalDate akhirMinggu = sekarang.with(DayOfWeek.SUNDAY);

        for (ManajerCSV.Tugas tugas : semuaTugas) {
            try {
                LocalDate tanggal = LocalDate.parse(tugas.getTanggal());
                DayOfWeek hari = tanggal.getDayOfWeek();

                if (!tanggal.isBefore(awalMinggu) && !tanggal.isAfter(akhirMinggu)) {
                    totalTugas.put(hari, totalTugas.get(hari) + 1);
                    if (tugas.isSelesai()) {
                        tugasSelesai.put(hari, tugasSelesai.get(hari) + 1);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error parsing tanggal: " + e.getMessage());
            }
        }

        Map<DayOfWeek, Double> persentase = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            int total = totalTugas.get(day);
            int selesai = tugasSelesai.get(day);
            double persen = (total == 0) ? 0 : (selesai * 100.0 / total);
            persentase.put(day, persen);
        }

        return persentase;
    }

    private void animatePercentageText(double targetPercentage) {
        targetPercentage = Math.max(0, Math.min(100, targetPercentage));
        int targetValue = (int) Math.round(targetPercentage);

        Timeline timeline = new Timeline();

        int steps = 20;
        int durationMs = 1500;

        for (int i = 0; i <= steps; i++) {
            double progress = (double) i / steps;
            int currentValue = (int) (targetValue * progress);

            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(durationMs * i / steps),
                            event -> persen.setText(String.format("%02d%%", currentValue))
                    )
            );
        }

        Timeline quickCounter = new Timeline(
                new KeyFrame(Duration.millis(100), event -> {
                    int randomValue = (int) (Math.random() * 100);
                    persen.setText(String.format("%02d%%", randomValue));
                })
        );
        quickCounter.setCycleCount(5);

        new SequentialTransition(quickCounter, timeline).play();
    }

    private void animateProgressBar(double targetProgress) {
        persentase.setProgress(0);
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(persentase.progressProperty(), targetProgress,
                                Interpolator.EASE_BOTH)
                )
        );
        timeline.play();
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
        Rectangle clip = new Rectangle(0, 0, 0, diagramHarian.getHeight());
        lineNode.setVisible(true);
        lineNode.setClip(clip);

        Platform.runLater(() -> {
            Timeline revealLine = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(clip.widthProperty(), 0)),
                    new KeyFrame(Duration.seconds(2), new KeyValue(clip.widthProperty(),
                            diagramHarian.getWidth(), Interpolator.EASE_OUT))
            );
            revealLine.play();
        });
    }

    public void switchScene(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("statistik-bulanan.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void switchSceneTugas(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("MainMenuView.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
}