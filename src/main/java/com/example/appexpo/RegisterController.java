package com.example.appexpo;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML private VBox formContainer;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nameField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formContainer.setTranslateY(50);
        formContainer.setOpacity(0);
        formContainer.setScaleX(0.95);
        formContainer.setScaleY(0.95);

        TranslateTransition slide = new TranslateTransition(Duration.millis(600), formContainer);
        slide.setFromY(50);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.EASE_BOTH);

        FadeTransition fade = new FadeTransition(Duration.millis(600), formContainer);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(600), formContainer);
        scale.setFromX(0.95);
        scale.setFromY(0.95);
        scale.setToX(1);
        scale.setToY(1);
        scale.setInterpolator(Interpolator.EASE_OUT);

        new ParallelTransition(formContainer, slide, fade, scale).play();
    }

    @FXML
    private void handleRegister() {
        String email = emailField.getText().trim();
        String pass  = passwordField.getText();
        String name  = nameField.getText().trim();

        if (email.isEmpty() || pass.isEmpty() || name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Semua kolom harus diisi.");
            return;
        }

        int    defaultAge    = 0;
        double defaultHeight = 0.0;
        double defaultWeight = 0.0;

        User newUser = new User(
                email,
                pass,
                name,
                defaultAge,
                defaultHeight,
                defaultWeight
        );

        UserStore.addUser(newUser);

        showAlert(Alert.AlertType.INFORMATION, "Registrasi berhasil! Silakan login.");
        goToLogin();
    }

    @FXML
    private void goToLogin() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/appexpo/LoginView.fxml"));
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(msg);
        alert.showAndWait();
    }
}
