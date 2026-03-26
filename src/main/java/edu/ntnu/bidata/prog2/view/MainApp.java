package edu.ntnu.bidata.prog2.view;

import edu.ntnu.bidata.prog2.model.Stock;
import edu.ntnu.bidata.prog2.transaction.Transaction;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        // ROOT
        BorderPane root = new BorderPane();

        // TOP - PLAYER INFO
        HBox playerInfo = new HBox(10);
        playerInfo.setStyle("-fx-padding: 10;");

        Label icon = new Label("👤");
        Label title = new Label("Player Info");

        playerInfo.getChildren().addAll(icon, title);
        root.setTop(playerInfo);

        // LEFT - PLAYER DETAILS
        VBox left = new VBox(15);
        left.setPrefWidth(120);
        left.setStyle("-fx-padding: 15;");

        left.getChildren().addAll(
                new Label("Name: "),
                new Label("Money: "),
                new Label("Net Worth: "),
                new Label("Status: "),
                new Label("Week: ")
        );

        root.setLeft(left);

        // STOCKS BOX
        VBox stocksBox = new VBox(10);

        HBox stocksTitleRow = new HBox(10);
        stocksTitleRow.getChildren().addAll(
                new Label("🔍"),
                new Label("Stocks")
        );

        TextField search = new TextField();
        search.setPromptText("Search");

        TableView<Stock> stockTable = new TableView<>();

        TableColumn<Stock, String> symbolCol = new TableColumn<>("Symbol");
        symbolCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSymbol()));

        TableColumn<Stock, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSalesPrice().toString()));

        TableColumn<Stock, String> changeCol = new TableColumn<>("Change");
        changeCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getLatestPriceChange().toString()));

        stockTable.getColumns().addAll(symbolCol, priceCol, changeCol);

        stockTable.setPrefSize(250, 245);
        stockTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        stocksBox.getChildren().addAll(
                stocksTitleRow,
                new Separator(),
                search,
                stockTable
        );

        // PORTFOLIO BOX
        VBox portfolioBox = new VBox(10);

        HBox portfolioTitleRow = new HBox(10);
        portfolioTitleRow.getChildren().addAll(
                new Label("📊"),
                new Label("Portfolio")
        );

        TableView<String> portfolioTable = new TableView<>();

        TableColumn<String, String> pSymbolCol = new TableColumn<>("Symbol");
        TableColumn<String, String> qtyCol = new TableColumn<>("Qty");
        TableColumn<String, String> valueCol = new TableColumn<>("Value");

        portfolioTable.getColumns().addAll(pSymbolCol, qtyCol, valueCol);

        portfolioTable.setPrefSize(250, 250);
        portfolioTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        portfolioBox.getChildren().addAll(
                portfolioTitleRow,
                new Separator(),
                new Label("Owned Shares"),
                portfolioTable
        );

        // MAIN CONTENT (TOP ROW)
        HBox mainContent = new HBox(100, stocksBox, portfolioBox);
        mainContent.setAlignment(Pos.TOP_CENTER);

        // SELECTED STOCK
        VBox selectedBox = new VBox(10);
        selectedBox.setMaxWidth(600);

        HBox selectedTitleRow = new HBox(10);
        selectedTitleRow.getChildren().addAll(
                new Label("⭐"),
                new Label("Selected Stock")
        );

        HBox quantityRow = new HBox(10);
        TextField quantityField = new TextField();
        quantityField.setPrefWidth(120);

        quantityRow.getChildren().addAll(
                new Label("Quantity:"),
                quantityField
        );

        HBox buttonRow = new HBox(20);
        buttonRow.getChildren().addAll(
                new Button("BUY"),
                new Button("SELL"),
                new Button("NEXT WEEK")
        );

        selectedBox.getChildren().addAll(
                selectedTitleRow,
                new Separator(),
                new Label("Selected:"),
                new Label("Price | High | Low | Change"),
                quantityRow,
                buttonRow
        );

        // TRANSACTIONS
        VBox transactionsBox = new VBox(10);
        transactionsBox.setMaxWidth(600);

        HBox transactionTitleRow = new HBox(10);
        transactionTitleRow.getChildren().addAll(
                new Label("★"),
                new Label("Transactions")
        );

        TableView<Transaction> transactionTable = new TableView<>();

        TableColumn<Transaction, String> weekCol = new TableColumn<>("Week");
        weekCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getWeek())));

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getClass().getSimpleName()));

        TableColumn<Transaction, String> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getShare().getStock().getSymbol()));

        TableColumn<Transaction, String> qtyColX = new TableColumn<>("Qty");
        qtyColX.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getShare().getQuantity().toString()));

        TableColumn<Transaction, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getCalculator().calculateTotal().toString()));

        transactionTable.getColumns().addAll(
                weekCol, typeCol, stockCol, qtyColX, totalCol
        );

        transactionTable.setPrefHeight(120);
        transactionTable.setMaxWidth(600);

        transactionsBox.getChildren().addAll(
                transactionTitleRow,
                new Separator(),
                transactionTable
        );

        // CENTER
        VBox centerBox = new VBox(25);
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMaxWidth(800);

        centerBox.getChildren().addAll(
                mainContent,
                selectedBox,
                transactionsBox
        );

        root.setCenter(centerBox);

        // SCENE
        Scene scene = new Scene(root, 1000, 800);
        stage.setTitle("Millions");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}