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
        left.setSpacing(20);
        Label stocksTitle = new Label("Stocks: ");

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");

        // Table View for stocks
        TableView<String> stockTable = new TableView<>();

        // columns for the stocks table
        TableColumn<String, String> symbolCol = new TableColumn<>("Symbol");
        TableColumn<String, String> priceCol = new TableColumn<>("Price");
        TableColumn<String, String> changeCol = new TableColumn<>("Change");

        // add columns to the table
        stockTable.getColumns().addAll(symbolCol, priceCol, changeCol);

        left.getChildren().addAll(stocksTitle, searchField, stockTable);
        root.setLeft(left);

        // -----Right Bar-----
        VBox right = new VBox();
        right.setSpacing(10);

        Label portfolioTitle = new Label("Portfolio");

        // Table View for portfolio
        TableView<String> portfolioTable = new TableView<>();

        // columns for the portfolio table
        TableColumn<String, String> pSymbolCol = new TableColumn<>("Symbol");
        TableColumn<String, String> qtyCol = new TableColumn<>("Quantity");
        TableColumn<String, String> valueCol = new TableColumn<>("Value");

        // add columns to the portfolio table
        portfolioTable.getColumns().addAll(pSymbolCol, qtyCol, valueCol);

        // add title and table to the right section
        right.getChildren().addAll(portfolioTitle, portfolioTable);

        // place at right
        root.setRight(right);

        // -----Center-----
        VBox center = new VBox();
        center.setSpacing(15);

        // title
        Label centerTitle = new Label("Selected Stock + Actions");


        // labels for the selected stock details
        Label price = new Label("Price: ");
        Label high = new Label("High: ");
        Label low = new Label("Low: ");
        Label change = new Label("Change: ");

        // label and text field for quantity input
        Label quantityLabel = new Label("Quantity:");
        TextField quantityField = new TextField();

        Button buyButton = new Button("Buy");
        Button sellButton = new Button("Sell");
        Button nextWeekButton = new Button("Next Week");

        // add all labels to the center section
        center.getChildren().addAll(centerTitle, price, high, low, change,
                quantityLabel, quantityField, buyButton, sellButton, nextWeekButton);

        // place at center
        root.setCenter(center);

        // -----Bottom-----
        VBox bottom = new VBox();
        bottom.setSpacing(20);

        Label transactionTitle = new Label("Transactions");

        // transactions table
        TableView<String> transactionTable = new TableView<>();

        TableColumn<String, String> weekCol = new TableColumn<>("Week");
        TableColumn<String, String> typeCol = new TableColumn<>("Type");
        TableColumn<String, String> stockCol = new TableColumn<>("Stock");
        TableColumn<String, String> qtyTransCol = new TableColumn<>("Qty");
        TableColumn<String, String> totalCol = new TableColumn<>("Total");

        transactionTable.getColumns().addAll(
                weekCol, typeCol, stockCol, qtyTransCol, totalCol
        );

        bottom.getChildren().addAll(transactionTitle, transactionTable);

        root.setBottom(bottom);

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