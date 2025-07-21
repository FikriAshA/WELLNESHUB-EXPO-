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

public class MenghitungKalkulator implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField beratBadanField;

    @FXML
    private RadioButton ringanRadio, sedangRadio, beratRadio;

    @FXML
    private TextArea hasilArea;

    @Override
    public void initialize(URL loc, ResourceBundle rb) {
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
    protected void hitungProtein() {
        try {
            double beratBadan = Double.parseDouble(beratBadanField.getText());
            double kebutuhanProtein = 0;

            if (ringanRadio.isSelected()) {
                kebutuhanProtein = beratBadan * 0.8;
            } else if (sedangRadio.isSelected()) {
                kebutuhanProtein = beratBadan * 1.3;
            } else if (beratRadio.isSelected()) {
                kebutuhanProtein = beratBadan * 1.6;
            } else {
                hasilArea.setText("Silakan pilih tingkat aktivitas.");
                return;
            }

            hasilArea.setText(String.format("Kebutuhan protein harian Anda: %.2f gram", kebutuhanProtein));

        } catch (NumberFormatException e) {
            hasilArea.setText("Masukkan berat badan yang valid (angka).");
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
