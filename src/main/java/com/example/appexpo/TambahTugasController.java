package com.example.appexpo;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class TambahTugasController implements Initializable {
    @FXML private TextField taskInputField;
    @FXML private DatePicker taskDatePicker;
    @FXML private Button addTaskButton;
    @FXML private VBox pendingTasksContainer;
    @FXML private VBox completedTasksContainer;
    @FXML private Button clearHistoryButton, pindahkeStatistik;
    @FXML private Button handleBack;
    @FXML private Pane rootPane;

    private final ManajerCSV csvManager = new ManajerCSV();
    private List<ManajerCSV.Tugas> semuaTugas = new ArrayList<>();

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        semuaTugas = csvManager.bacaSemuaTugas();
        tampilkanTugas();

        taskDatePicker.setValue(LocalDate.now());

        addTaskButton.setOnAction(e -> tambahTugasBaru());
        clearHistoryButton.setOnAction(e -> hapusRiwayat());

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

    private void tambahTugasBaru() {
        String namaTugas = taskInputField.getText().trim();
        LocalDate tanggal = taskDatePicker.getValue();

        if (!namaTugas.isEmpty() && tanggal != null) {
            ManajerCSV.Tugas tugasBaru = new ManajerCSV.Tugas(
                    tanggal.toString(),
                    namaTugas,
                    false
            );

            semuaTugas.add(tugasBaru);
            csvManager.simpanSemuaTugas(semuaTugas);
            tampilkanTugas();
            taskInputField.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Nama tugas dan tanggal harus diisi!");
            alert.showAndWait();
        }
    }

    private void tampilkanTugas() {
        pendingTasksContainer.getChildren().clear();
        completedTasksContainer.getChildren().clear();

        for (ManajerCSV.Tugas tugas : semuaTugas) {
            CheckBox checkBox = new CheckBox(tugas.toString());
            checkBox.setSelected(tugas.isSelesai());

            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                csvManager.updateStatusTugas(tugas, newVal);
                csvManager.simpanSemuaTugas(semuaTugas);
                tampilkanTugas();
            });

            if (tugas.isSelesai()) {
                completedTasksContainer.getChildren().add(checkBox);
            } else {
                pendingTasksContainer.getChildren().add(checkBox);
            }
        }
    }

    private void hapusRiwayat() {
        semuaTugas.removeIf(tugas -> tugas.isSelesai());
        csvManager.simpanSemuaTugas(semuaTugas);
        tampilkanTugas();
    }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) handleBack.getScene().getWindow();
            stage.getScene().setRoot(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        loadView("/com/example/appexpo/MainMenuView.fxml");
    }

    public void switchScene(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("statistik-bulanan.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
}