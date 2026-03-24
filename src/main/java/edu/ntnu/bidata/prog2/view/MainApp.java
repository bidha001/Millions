package edu.ntnu.bidata.prog2.view;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage){
        // Root layout
        BorderPane root = new BorderPane();

        // ---Top(Player info)---
        VBox playerInfo = new VBox();
        playerInfo.setSpacing(10);

        // ---Row 1: Title---
        HBox titleRow = new HBox();
        titleRow.setSpacing(10);

        Label icon = new Label("👤");
        Label title = new Label("Player Info");

        titleRow.getChildren().addAll(icon, title);

        // ---Row 2: Player details---
        HBox infoRow = new HBox();
        infoRow.setSpacing(20);

        Label name = new Label("Name: ");
        Label money = new Label("Money: ");
        Label netWorth = new Label("Net Worth: ");
        Label status = new Label("Status: ");
        Label week = new Label("Week: ");

        Separator line = new Separator();

        infoRow.getChildren().addAll(name, money, netWorth, status, week);

        playerInfo.getChildren().addAll(titleRow, infoRow, line);

        root.setTop(playerInfo);


        // Scene
        Scene scene = new Scene(root, 1000, 700);
        // set the title in the window.
        stage.setTitle("Millions");
        // set the scene in the window.
        stage.setScene(scene);
        // display the window.
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}