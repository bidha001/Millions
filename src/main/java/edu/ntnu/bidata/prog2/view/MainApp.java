package edu.ntnu.bidata.prog2.view;

import edu.ntnu.bidata.prog2.model.Stock;
import edu.ntnu.bidata.prog2.transaction.Transaction;
import javafx.application.Application;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;

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
        wrapper.setSpacing(20);

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

        // --- Sample data ---
        Stock apple = new Stock("Apple", "AAPL", new BigDecimal("150"));
        apple.addNewSalesPrice(new BigDecimal("152"));

        Stock tesla = new Stock("Tesla", "TSLA", new BigDecimal("200"));
        tesla.addNewSalesPrice(new BigDecimal("195"));

        // Add to table
        stockTable.getItems().addAll(apple, tesla);

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

        // ---Selected Stock + Actions Section---
        VBox actionsBox = new VBox();
        actionsBox.setSpacing(10);

        actionsBox.setMaxHeight(Region.USE_PREF_SIZE);
        // Line on top
        Separator topLine = new Separator();

        // Title row
        HBox titleRow2 = new HBox();
        titleRow2.setSpacing(10);

        Label star = new Label("★");
        Label actionsTitle = new Label("Selected Stock + Actions");

        titleRow2.getChildren().addAll(star, actionsTitle);

        // Line under title
        Separator bottomLine = new Separator();

        // Info text (placeholder for now)
        Label selected = new Label("Selected:");
        Label details = new Label("Price | High | Low | Change");

        // Quantity input
        HBox quantityRow = new HBox();
        quantityRow.setSpacing(10);

        Label quantityLabel = new Label("Quantity:");
        TextField quantityField = new TextField();
        quantityField.setPrefWidth(100);

        quantityRow.getChildren().addAll(quantityLabel, quantityField);

        // Buttons
        HBox buttonRow = new HBox();
        buttonRow.setSpacing(20);

        Button buyBtn = new Button("BUY");
        Button sellBtn = new Button("SELL");
        Button nextWeekBtn = new Button("NEXT WEEK");

        buttonRow.getChildren().addAll(buyBtn, sellBtn, nextWeekBtn);

        // Add everything
        actionsBox.getChildren().addAll(topLine, titleRow2, bottomLine, selected, details, quantityRow, buttonRow);

        wrapper.getChildren().add(actionsBox);

        root.setCenter(wrapper);

        //--- Transactions ---
        VBox transactionsBox = new VBox();
        transactionsBox.setSpacing(10);
        // Title row
        HBox transactionsTitleRow = new HBox();
        transactionsTitleRow.setSpacing(10);

        Label transactionIcon = new Label("★");
        Label transactionsTitle= new Label("Transactions");

        transactionsTitleRow.getChildren().addAll(transactionIcon, transactionsTitle);

        //
        TableColumn<Transaction, String> weekCol = new TableColumn<>("Week");
        weekCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getWeek()))
        );

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getClass().getSimpleName())
        );

        TableColumn<Transaction, String> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getShare().getStock().getSymbol()
                )
        );

        TableColumn<Transaction, String> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        String.valueOf(data.getValue().getShare().getQuantity())
                )
        );

        TableColumn<Transaction, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getCalculator()
                                .calculateTotal(data.getValue().getShare())
                                .toString()
                )
        );

        transactionTable.getColumns().addAll(
                weekCol, typeCol, stockCol, qtyCol, totalCol
        );

        transactionTable.setPrefHeight(150);

        transactionBox.getChildren().addAll(
                new Separator(),
                transactionTitleRow,
                new Separator(),
                transactionTable
        );

        wrapper.getChildren().addAll(
                mainContent,
                actionsBox,
                transactionBox
        );


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