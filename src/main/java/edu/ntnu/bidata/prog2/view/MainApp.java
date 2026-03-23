package edu.ntnu.bidata.prog2.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage){
        // Root layout for the application.
        BorderPane root = new BorderPane();

        // Scene
        Scene scene = new Scene(root, 1000, 700);

        // Stage setup
        stage.setTitle("Millions");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}