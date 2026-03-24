package edu.ntnu.bidata.prog2.view;

import javafx.application.Application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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


        // ---Main Content: Stocks and Portfolio---
        VBox wrapper = new VBox();
        // center horizontally
        wrapper.setAlignment(Pos.TOP_CENTER);

        HBox mainContent = new HBox();
        mainContent .setSpacing(100);
        mainContent.setMaxWidth(800);
        mainContent.setAlignment(Pos.CENTER);

        // ---Left (Stocks)---
        VBox stocksBox = new VBox();
        stocksBox.setSpacing(10);

        // Title row
        HBox stocksTitleRow = new HBox();
        stocksTitleRow.setSpacing(10);

        Label stocksIcon = new Label("🔍");
        Label stocksTitle = new Label("Stocks");

        stocksTitleRow.getChildren().addAll(stocksIcon, stocksTitle);
        // Line under title
        Separator stocksLine = new Separator();

        // Search bar
        TextField search = new TextField();
        search.setPromptText("Search");

        // Table placeholder
        TableView<String> stockTable = new TableView<>();

        TableColumn<String, String> symbolCol = new TableColumn<>("Symbol");
        TableColumn<String, String> priceCol = new TableColumn<>("Price");
        TableColumn<String, String> changeCol = new TableColumn<>("Change");

        stockTable.getColumns().addAll(symbolCol, priceCol, changeCol);

        // Adding all to the stock box
        stocksBox.getChildren().addAll(stocksTitleRow, stocksLine, search, stockTable);

        // ---Right (Portfolio)---
        VBox portfolioBox = new VBox();
        portfolioBox.setSpacing(10);

        // Title row
        HBox portfolioTitleRow = new HBox();
        portfolioTitleRow.setSpacing(10);

        Label portfolioIcon = new Label("📊");
        Label portfolioTitle = new Label("Portfolio");

        portfolioTitleRow.getChildren().addAll(portfolioIcon, portfolioTitle);

        // Line under title
        Separator portfolioLine = new Separator();

        // Subtitle
        Label ownedSharesTitle = new Label("Owned Shares");

        // Table placeholder
        TableView<String> portfolioTable = new TableView<>();

        TableColumn<String, String> pSymbolCol = new TableColumn<>("Symbol");
        TableColumn<String, String> qtyCol = new TableColumn<>("Qty");
        TableColumn<String, String> valueCol = new TableColumn<>("Value");

        portfolioTable.getColumns().addAll(pSymbolCol, qtyCol, valueCol);



        // Adding all to the portfolio box
        portfolioBox.getChildren().addAll(portfolioTitleRow, portfolioLine, ownedSharesTitle, portfolioTable);

        // Add both sections to the main content
        mainContent.getChildren().addAll(stocksBox, portfolioBox);

        // Add both side to the main content
        wrapper.getChildren().addAll(mainContent);
        root.setCenter(wrapper);


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