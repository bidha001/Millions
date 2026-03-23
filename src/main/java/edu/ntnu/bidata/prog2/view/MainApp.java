package edu.ntnu.bidata.prog2.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage){
        // Root layout for the application.
        BorderPane root = new BorderPane();

        // -----Top Bar-----
        HBox topBar = new HBox();

        // spacing between elements
        topBar.setSpacing(20);

        // labels for the top bar
        Label name = new Label("Name: ");
        Label money = new Label("Money: ");
        Label netWorth = new Label("Net Worth: ");
        Label status = new Label("Status: ");
        Label week = new Label("Week: ");

        // add all labels to the top bar
        topBar.getChildren().addAll(name, money, netWorth, status, week);
        // place at top
        root.setTop(topBar);

        // -----Left Bar-----
        VBox left = new VBox();
        Label stocksTitle = new Label("Stocks: ");
        left.getChildren().add(stocksTitle);
        root.setLeft(left);


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