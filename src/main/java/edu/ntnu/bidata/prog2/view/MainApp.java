package edu.ntnu.bidata.prog2.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage){


        // Stage setup
        stage.setTitle("Millions");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}