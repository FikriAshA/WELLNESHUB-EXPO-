package com.example.appexpo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MainLogin extends Application {
    @Override
    public void start(Stage Stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/appexpo/LoginView.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        scene.setFill(Color.web("#E6F7FF"));

        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm()
        );

        Stage.setTitle("Wellness Hub");
        Stage.setScene(scene);
        Stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}