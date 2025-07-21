package com.example.appexpo;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Pane borderPane;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        borderPane.setTranslateX(-50);
        borderPane.setOpacity(0);
        borderPane.setScaleX(0.95);
        borderPane.setScaleY(0.95);

        TranslateTransition slide = new TranslateTransition(Duration.millis(600), borderPane);
        slide.setFromX(-50);
        slide.setToX(0);
        slide.setInterpolator(Interpolator.EASE_BOTH);

        FadeTransition fade = new FadeTransition(Duration.millis(600), borderPane);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(600), borderPane);
        scale.setFromX(0.95);
        scale.setFromY(0.95);
        scale.setToX(1);
        scale.setToY(1);
        scale.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition pt = new ParallelTransition(borderPane, slide, fade, scale);
        pt.play();
    }

    @FXML
    private void gotoHome() {
        load("/com/example/appexpo/MainMenuView.fxml");
    }

    @FXML
    private void gotoSettings() {
        load("/com/example/appexpo/SettingsView.fxml");
    }

    @FXML
    private void onLogout(ActionEvent evt) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Yakin ingin logout?", ButtonType.CANCEL, ButtonType.OK);
        confirm.setTitle("Konfirmasi Logout");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Session.clear();
                try {
                    Parent login = FXMLLoader.load(
                            getClass().getResource("/com/example/appexpo/LoginView.fxml"));
                    Stage stage = (Stage) ((Node) evt.getSource())
                            .getScene().getWindow();
                    stage.getScene().setRoot(login);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void gotoArticleTips() {
        load("/com/example/appexpo/ArtikelMenuView.fxml");
    }

    @FXML
    private void gotoProfile() {
        load("/com/example/appexpo/ProfileView.fxml");
    }

    @FXML
    private void gotoHabitTracker() {
        load("/com/example/appexpo/tambahTugas.fxml");
    }

    @FXML
    private void gotoStats() {
        load("/com/example/appexpo/statistik-bulanan.fxml");
    }

    @FXML
    private void gotoBMICalculator() {
        load("/com/example/appexpo/bmi-view.fxml");
    }

    @FXML
    private void gotoCalorieNeeds() {
        load("/com/example/appexpo/kalori-view.fxml");}

    @FXML
    private void gotoProteinNeeds() {
        load("/com/example/appexpo/MenghitungKalkulator.fxml");
    }

    @FXML
    private void gotoArticle1() {
        load( "/com/example/appexpo/Artikel1View.fxml");
    }

    @FXML
    private void gotoArticle3() {
        load( "/com/example/appexpo/Artikel3View.fxml");
    }

    private void load(String resourcePath) {
        try {
            Stage stage = (Stage) borderPane.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(resourcePath));
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
