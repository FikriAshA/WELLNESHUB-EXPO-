package com.example.appexpo;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ArtikelMenuController implements Initializable {
    @FXML
    private AnchorPane rootPane;


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
    private void gotoArticle1() {
        loadView( "/com/example/appexpo/Artikel1View.fxml");
    }

    @FXML
    private void gotoArticle2() {
        loadView("/com/example/appexpo/Artikel2View.fxml");
    }

    @FXML
    private void gotoArticle3() {
        loadView("/com/example/appexpo/Artikel3View.fxml");
    }

    @FXML
    private void gotoTips1() {
        loadView("/com/example/appexpo/Tips1View.fxml");
    }

    @FXML
    private void handleBack() {
        loadView("/com/example/appexpo/MainMenuView.fxml");
    }

}
