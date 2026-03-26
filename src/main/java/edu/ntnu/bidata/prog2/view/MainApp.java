package edu.ntnu.bidata.prog2.view;

import edu.ntnu.bidata.prog2.model.Stock;
import javafx.application.Application;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage){
        // Root layout
        BorderPane root = new BorderPane();

        // ---Top(Player info)---
        HBox playerInfo = new HBox();
        playerInfo.setSpacing(10);
        playerInfo.setStyle("-fx-padding: 10;");

        Label icon = new Label("👤");
        Label title = new Label("Player Info");

        playerInfo.getChildren().addAll(icon, title);

        root.setTop(playerInfo);

        // ---Player Details on left---
        VBox left = new VBox();
        left.setSpacing(15);
        left.setPrefWidth(75);
        left.setStyle("-fx-padding: 15;");

        Label name = new Label("Name: ");
        Label money = new Label("Money: ");
        Label netWorth = new Label("Net Worth: ");
        Label status = new Label("Status: ");
        Label week = new Label("Week: ");

        left.getChildren().addAll(name, money, netWorth, status, week);
        root.setLeft(left);


        // ---Main Content: Stocks and Portfolio---
        VBox wrapper = new VBox();

        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setSpacing(20);

        HBox mainContent = new HBox();
        mainContent .setSpacing(100);
        mainContent.setAlignment(Pos.TOP_CENTER);

        // ---Left (Stocks)---
        VBox stocksBox = new VBox();
        stocksBox.setSpacing(10);

        // Stock title row
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
        TableView<Stock> stockTable = new TableView<>();

        TableColumn<Stock, String> symbolCol = new TableColumn<>("Symbol");
        symbolCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSymbol())
        );

        TableColumn<Stock, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSalesPrice().toString())
        );

        TableColumn<Stock, String> changeCol = new TableColumn<>("Change");
        changeCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getLatestPriceChange().toString())
        );

        stockTable.getColumns().addAll(symbolCol, priceCol, changeCol);

        // --- SIZE SETTINGS FOR STOCKS ---
        stockTable.setPrefHeight(245);
        stockTable.setPrefWidth(250);

        symbolCol.setPrefWidth(100);
        priceCol.setPrefWidth(100);
        changeCol.setPrefWidth(100);

        stockTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Adding all to the stock box
        stocksBox.getChildren().addAll(stocksTitleRow, stocksLine, search, stockTable);


        // ---Right (Portfolio)---
        VBox portfolioBox = new VBox();
        portfolioBox.setSpacing(10);

        // Portfolio title row
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

        // --- SIZE SETTINGS FOR PORTFOLIO ---
        portfolioTable.setPrefHeight(250);
        portfolioTable.setPrefWidth(250);

        pSymbolCol.setPrefWidth(100);
        qtyCol.setPrefWidth(80);
        valueCol.setPrefWidth(120);

        portfolioTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Adding all to the portfolio box
        portfolioBox.getChildren().addAll(portfolioTitleRow, portfolioLine, ownedSharesTitle, portfolioTable);

        // Add both sections to the main content
        mainContent.getChildren().addAll(stocksBox, portfolioBox);
        root.setCenter(mainContent);

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