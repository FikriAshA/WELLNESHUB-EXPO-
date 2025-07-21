package com.example.appexpo;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BMIController implements Initializable {

    @FXML
    private TextField weightField;

    @FXML
    private TextField heightField;

    @FXML
    private Button hitungButton;

    @FXML ComboBox<String> genderBox;

    @FXML
    private AnchorPane rootPane;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        if (genderBox != null) {
            genderBox.getItems().addAll("Laki-Laki", "Perempuan");
        }

        rootPane.setTranslateX(-50);
        rootPane.setOpacity(0);
        rootPane.setScaleX(0.95);
        rootPane.setScaleY(0.95);

        TranslateTransition slide = new TranslateTransition(Duration.millis(600), rootPane);
        slide.setFromX(-50);
        slide.setToX(0);
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

        ParallelTransition pt = new ParallelTransition(rootPane, slide, fade, scale);
        pt.play();
    }

    @FXML
    private void calculateBMI(ActionEvent event) {
        try {
            double berat  = Double.parseDouble(weightField.getText());
            double tinggi = Double.parseDouble(heightField.getText()) / 100.0;
            double bmi    = berat / (tinggi * tinggi);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("result-view.fxml"));
            Parent root = loader.load();

            ResultController controller = loader.getController();
            controller.setBMIResult(bmi,
                    Double.parseDouble(heightField.getText()), berat);

            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);

        } catch (NumberFormatException | NullPointerException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Masukkan tinggi dan berat yang valid.").showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
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
    private void goBack() {
        loadView("/com/example/appexpo/MainMenuView.fxml");
    }

}
