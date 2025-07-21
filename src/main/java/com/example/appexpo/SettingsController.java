package com.example.appexpo;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML private TextField emailField;
    @FXML private PasswordField oldPassField;
    @FXML private PasswordField newPassField;
    @FXML private AnchorPane rootAnchor;


    private User currentUser;

    @Override
    public void initialize(URL loc, ResourceBundle rb) {
        rootAnchor.setOpacity(0);
        rootAnchor.setTranslateY(50);

        FadeTransition fade = new FadeTransition(Duration.millis(500), rootAnchor);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(500), rootAnchor);
        slide.setFromY(50);
        slide.setToY(0);

        fade.play();
        slide.play();;

        currentUser = Session.getCurrentUser();
    }

    @FXML
    private void handleUpdateEmail(ActionEvent evt) {
        String newEmail = emailField.getText().trim();
        if (newEmail.isEmpty()) {
            showError("Email baru tidak boleh kosong");
            return;
        }
        Map<String,User> all = UserStore.loadUsers();
        if (all.containsKey(newEmail)) {
            showError("Email sudah digunakan");
            return;
        }

        User updated = new User(
                newEmail,
                currentUser.getPassword(),
                currentUser.getName(),
                currentUser.getAge(),
                currentUser.getHeight(),
                currentUser.getWeight()
        );
        UserStore.deleteUser(currentUser.getEmail());
        UserStore.addUser(updated);
        Session.setCurrentUser(updated);
        showInfo("Email berhasil diperbarui");
        emailField.clear();
    }

    @FXML
    private void handleUpdatePassword(ActionEvent evt) {
        String oldPass = oldPassField.getText();
        String newPass = newPassField.getText();

        if (oldPass.isEmpty()) {
            showError("Password lama tidak boleh kosong");
            return;
        }

        if (newPass.isEmpty()) {
            showError("Password baru tidak boleh kosong");
            return;
        }

        if (!currentUser.checkPassword(oldPass)) {
            showError("Password lama salah");
            return;
        }

        User updated = new User(
                currentUser.getEmail(),
                newPass,
                currentUser.getName(),
                currentUser.getAge(),
                currentUser.getHeight(),
                currentUser.getWeight()
        );
        UserStore.updateUser(updated);
        Session.setCurrentUser(updated);
        showInfo("Password berhasil diperbarui");
        oldPassField.clear(); newPassField.clear();
    }

    @FXML
    private void handleDeleteAccount(ActionEvent evt) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Yakin ingin menghapus akun? Data tidak dapat dikembalikan.");
        confirm.showAndWait().ifPresent(b -> {
            if (b == javafx.scene.control.ButtonType.OK) {
                UserStore.deleteUser(currentUser.getEmail());
                Session.clear();
                try {
                    Stage stage = (Stage) ((Node) evt.getSource())
                            .getScene().getWindow();
                    Parent login = FXMLLoader.load(
                            getClass().getResource("/com/example/appexpo/LoginView.fxml"));
                    stage.getScene().setRoot(login);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            Parent main = FXMLLoader.load(
                    getClass().getResource("/com/example/appexpo/MainMenuView.fxml"));
            stage.getScene().setRoot(main);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}

