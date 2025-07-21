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
import java.time.LocalDateTime;
import java.util.Map;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private VBox formContainer;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formContainer.setTranslateX(-50);
        formContainer.setOpacity(0);
        formContainer.setScaleX(0.95);
        formContainer.setScaleY(0.95);

        TranslateTransition slide = new TranslateTransition(Duration.millis(600), formContainer);
        slide.setFromX(-50);
        slide.setToX(0);
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

        ParallelTransition pt = new ParallelTransition(formContainer, slide, fade, scale);
        pt.play();
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String pass  = passwordField.getText();

        Map<String, User> users = UserStore.loadUsers();
        User u = users.get(email);

        if (u != null && u.checkPassword(pass)) {
            Session.setLastLoginDate(LocalDateTime.now());
            Session.setCurrentUser(u);
            goToMainView();
        } else {
            showError();
        }
    }

    @FXML
    private void goToRegister() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/appexpo/RegisterView.fxml"));
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToMainView() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/appexpo/MainMenuView.fxml"));
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Gagal");
        alert.setHeaderText("Email atau password salah");
        alert.showAndWait();
        passwordField.clear();
    }
}
