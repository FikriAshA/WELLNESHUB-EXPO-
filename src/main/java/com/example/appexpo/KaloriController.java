package com.example.appexpo;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class KaloriController implements Initializable {
    @FXML private TextField beratField, tinggiField, umurField;
    @FXML private ChoiceBox<String> jenisKelaminBox, aktivitasBox;
    @FXML private AnchorPane rootPane;
    @FXML private Label hasil;

    @FXML
    public void initialize(URL loc, ResourceBundle rb) {
        jenisKelaminBox.getItems().addAll("Laki-laki", "Perempuan");
        aktivitasBox.getItems().addAll("Sangat Ringan", "Ringan", "Sedang", "Berat", "Sangat Berat");

        rootPane.setOpacity(0);
        rootPane.setTranslateY(50);

        FadeTransition fade = new FadeTransition(Duration.millis(500), rootPane);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(500), rootPane);
        slide.setFromY(50);
        slide.setToY(0);

        fade.play();
        slide.play();
    }

    @FXML
    public void hitungKalori() {
        try {
            double berat = Double.parseDouble(beratField.getText());
            double tinggi = Double.parseDouble(tinggiField.getText());
            int umur = Integer.parseInt(umurField.getText());
            String kelamin = jenisKelaminBox.getValue();
            String aktivitas = aktivitasBox.getValue();

            double bmr;
            if (kelamin.equals("Laki-laki")) {
                bmr = 10 * berat + 6.25 * tinggi - 5 * umur + 5;
            } else {
                bmr = 10 * berat + 6.25 * tinggi - 5 * umur - 161;
            }

            double faktorAktivitas;
            switch (aktivitas) {
                case "Sangat Ringan":
                    faktorAktivitas = 1.2;
                    break;
                case "Ringan":
                    faktorAktivitas = 1.375;
                    break;
                case "Sedang":
                    faktorAktivitas = 1.55;
                    break;
                case "Berat":
                    faktorAktivitas = 1.725;
                    break;
                case "Sangat Berat":
                    faktorAktivitas = 1.9;
                    break;
                default:
                    faktorAktivitas = 1.0;
                    break;
            }

            double tdee = bmr * faktorAktivitas;
            double defisit = tdee - 500;
            double surplus = tdee + 500;

            hasil.setText(String.format(
                    "BMR Anda: %.2f kalori\n" +
                            "TDEE Anda: %.2f kalori\n" +
                            "Saran Defisit (turun BB): %.2f kalori\n" +
                            "Saran Surplus (naik BB): %.2f kalori",
                    bmr, tdee, defisit, surplus));
        } catch (Exception e) {
            hasil.setText("Pastikan semua data diisi dengan benar.");
        }
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
    private void handleBack() {
        loadView("/com/example/appexpo/MainMenuView.fxml");
    }
}
