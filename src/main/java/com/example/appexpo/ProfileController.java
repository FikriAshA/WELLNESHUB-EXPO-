package com.example.appexpo;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML private Label greetingLabel;
    @FXML private TextField emailField;
    @FXML private TextField nameField;
    @FXML private TextField umurField;
    @FXML private TextField heightField;
    @FXML private TextField weightField;
    @FXML private Pane rootPane;
    @FXML private Label lblTipOfDay;
    @FXML private Label lblLastLogin;
    @FXML private Label lblLastUpdate;

    private User currentUser;
    private String[] healthTips2 = new String[3];
    private void initializeTips() {
        healthTips2[0] = "Minum 8 gelas air per hari";
        healthTips2[1] = "Lakukan olahraga ringan 30 menit";
        healthTips2[2] = "Istirahat cukup 7–8 jam";
    }

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

        initializeTips();

        int idx = LocalDate.now().getDayOfMonth() % healthTips2.length;
        lblTipOfDay.setText(healthTips2[idx]);

        LocalDateTime lastLogin = Session.getLastLoginDate();
        lblLastLogin.setText(lastLogin!=null
                ? lastLogin.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))
                : "—");

        LocalDateTime lastUpd = Session.getProfileUpdateDate();
        lblLastUpdate.setText(lastUpd!=null
                ? lastUpd.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))
                : "—");


        currentUser = Session.getCurrentUser();
        if (currentUser == null) return;

        greetingLabel.setText("Halo, " + currentUser.getName() + "!");

        emailField.setText(currentUser.getEmail());
        nameField.setText(currentUser.getName());
        umurField.setText(String.valueOf(currentUser.getAge()));
        heightField.setText(String.valueOf(currentUser.getHeight()));
        weightField.setText(String.valueOf(currentUser.getWeight()));
    }

    @FXML
    private void handleSave(ActionEvent evt) {
        try {
            String name = nameField.getText().trim();
            int age    = Integer.parseInt(umurField.getText().trim());
            double h   = Double.parseDouble(heightField.getText().trim());
            double w   = Double.parseDouble(weightField.getText().trim());

            User updated = new User(
                    currentUser.getEmail(),
                    currentUser.getPassword(),
                    name, age, h, w
            );
            Session.setProfileUpdateDate(LocalDateTime.now());
            UserStore.updateUser(updated);
            Session.setCurrentUser(updated);

            new Alert(Alert.AlertType.INFORMATION,
                    "Profil berhasil diperbarui")
                    .showAndWait();

        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR,
                    "Umur, tinggi, dan berat harus angka valid")
                    .showAndWait();
        }
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
}
