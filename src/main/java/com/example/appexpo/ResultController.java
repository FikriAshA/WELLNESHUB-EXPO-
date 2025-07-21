package com.example.appexpo;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class ResultController implements Initializable {

    @FXML
    private Label resultLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Label bmiPointer;
    @FXML private AnchorPane rootPane;

    private double bmi;
    private final TreeMap<Double, String> bmiCategories = new TreeMap<>();

    @Override
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
    }

    public ResultController() {
        initBMICategories();
    }

    public void setBMIResult(double bmi, double tinggi, double berat) {
        this.bmi = bmi;

        String kategori = getBMICategory(bmi);

        resultLabel.setText(String.format("BMI: %.1f (%s)", bmi, kategori));

        if (bmi < 18.5) {
            infoLabel.setText("Berat badan kamu di bawah normal. " +
                    "Cobalah konsumsi makanan bergizi dan konsultasikan dengan ahli gizi!");
        } else if (bmi < 25) {
            infoLabel.setText("Berat badan kamu ideal. " +
                    "Pertahankan pola hidup sehat dan tetap aktif ya!");
        } else if (bmi < 30) {
            infoLabel.setText("Kamu sedikit kelebihan berat badan. " +
                    "Coba jaga pola makan dan aktif berolahraga!");
        } else {
            infoLabel.setText("Berat badan kamu tergolong obesitas. " +
                    "Disarankan konsultasi dengan dokter untuk saran yang tepat.");
        }

        double widthKurus = 129.5;
        double widthNormal = 44.8;
        double widthOver = 34.3;
        double widthObes = 70.0;

        double relativeX;
        if (bmi <= 18.4) {
            double ratio = bmi / 18.4;
            relativeX = ratio * widthKurus;
        } else if (bmi <= 24.9) {
            double ratio = (bmi - 18.5) / (24.9 - 18.5);
            relativeX = widthKurus + ratio * widthNormal;
        } else if (bmi <= 29.9) {
            double ratio = (bmi - 25) / (29.9 - 25);
            relativeX = widthKurus + widthNormal + ratio * widthOver;
        } else {
            double ratio = Math.min((bmi - 30) / 10.0, 1.0);
            relativeX = widthKurus + widthNormal + widthOver + ratio * widthObes;
        }

        double barStartX = 500.0;
        double pointerOffset = 10.0;
        bmiPointer.setLayoutX(barStartX + relativeX - pointerOffset);
    }

    @FXML
    public void ulangButton(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("bmi-view.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBMICategories() {
        bmiCategories.put(18.4, "Berat Rendah");
        bmiCategories.put(24.9, "Normal");
        bmiCategories.put(29.9, "Berat Berlebih");
        bmiCategories.put(Double.MAX_VALUE, "Obesitas");
    }

    private String getBMICategory(double bmi) {
        for (Double batas : bmiCategories.keySet()) {
            if (bmi <= batas) {
                return bmiCategories.get(batas);
            }
        }
        return "Tidak diketahui";
    }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.getScene().setRoot(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        loadView("/com/example/appexpo/MainMenuView.fxml");
    }
}