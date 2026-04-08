package edu.ntnu.bidata.prog2.ui.view;

import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;
import edu.ntnu.bidata.prog2.transaction.Transaction;
import edu.ntnu.bidata.prog2.ui.controller.WindowViewController;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowView extends Application {
    private WindowViewController controller;
    private TableView<Stock> stockTable;
    private TableView<Share> portfolioTable;
    private TableView<Transaction> transactionTable;
    private Button buyButton;
    private Button sellButton;
    private Button nextWeekButton;
    private Label nameLabel;
    private Label moneyLabel;
    private Label netWorthLabel;
    private Label statusLabel;
    private Label weekLabel;

    @Override
    public void start(Stage stage) {

        controller = new WindowViewController();

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
        left.setPrefWidth(200);
        left.setStyle("-fx-padding: 15;");

        nameLabel = new Label("Name: ");
        moneyLabel = new Label("Money: ");
        netWorthLabel = new Label("Net Worth: ");
        statusLabel = new Label("Status: ");
        weekLabel = new Label("Week: ");

        Button startButton = new Button("New Game");
        startButton.setMaxWidth(Double.MAX_VALUE);

        startButton.setOnAction(e-> showStartDialog(stage));

        left.getChildren().addAll(
                nameLabel, moneyLabel, netWorthLabel, statusLabel, weekLabel, startButton
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

        stockTable = new TableView<>();

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


        search.textProperty().addListener((observable, oldValue, newValue) -> {

            stockTable.getItems().clear();

            if (controller.getExchange() != null) {
                stockTable.getItems().addAll(
                        controller.getExchange().findStocks(newValue)
                );
            }
        });

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

        portfolioTable = new TableView<>();

        TableColumn<Share, String> pSymbolCol = new TableColumn<>("Symbol");
        pSymbolCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStock().getSymbol()));

        TableColumn<Share, String> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getQuantity().toString()));

        TableColumn<Share, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getStock().getSalesPrice()
                                .multiply(data.getValue().getQuantity())
                                .toString()
                ));

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

        // --- SELECTED STOCK ---
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

        buyButton = new Button("BUY");
        sellButton = new Button("SELL");
        nextWeekButton = new Button("NEXT WEEK");

        buyButton.setDisable(true);
        sellButton.setDisable(true);
        nextWeekButton.setDisable(true);

        HBox buttonRow = new HBox(20);
        buttonRow.getChildren().addAll(
                buyButton,
                sellButton,
                nextWeekButton
        );

        nextWeekButton.setOnAction(e -> {
            controller.nextWeek();
            updatePlayerInfo();
            updatePortfolioTable();
            updateTransactionTable();
            stockTable.refresh();
        });

        buyButton.setOnAction(e -> {

            if (controller.getPlayer() == null) {
                new Alert(Alert.AlertType.WARNING, "Start a game first!").showAndWait();
                return;
            }

            Stock selectedStock = stockTable.getSelectionModel().getSelectedItem();

            if (selectedStock == null) {
                new Alert(Alert.AlertType.WARNING, "Select a stock first!").showAndWait();
                return;
            }

            try {
                BigDecimal quantity = new BigDecimal(quantityField.getText());

                if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                    new Alert(Alert.AlertType.WARNING, "Quantity must be greater than 0!")
                            .showAndWait();
                    return;
                }

                Share share = new Share(
                        selectedStock,
                        quantity,
                        selectedStock.getSalesPrice(),
                        null
                );

                controller.buy(share);
                updatePlayerInfo();
                updatePortfolioTable();
                updateTransactionTable();
                quantityField.clear();

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Invalid quantity!")
                        .showAndWait();
            }
        });

        sellButton.setOnAction(e -> {

            if (controller.getPlayer() == null) {
                new Alert(Alert.AlertType.WARNING, "Start a game first!")
                        .showAndWait();
                return;
            }

            Share selectedShare = portfolioTable.getSelectionModel().getSelectedItem();

            if (selectedShare == null) {
                new Alert(Alert.AlertType.WARNING, "Select a share from portfolio!")
                        .showAndWait();
                return;
            }

            try {
                BigDecimal quantity = new BigDecimal(quantityField.getText());

                if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                    new Alert(Alert.AlertType.WARNING, "Quantity must be greater than 0!")
                            .showAndWait();
                    return;
                }

                //
                if (quantity.compareTo(selectedShare.getQuantity()) > 0) {
                    new Alert(Alert.AlertType.ERROR, "You don't own that many shares!")
                            .showAndWait();
                    return;
                }

                controller.sell(selectedShare.getStock(), quantity);

                updatePlayerInfo();
                updatePortfolioTable();
                updateTransactionTable();
                quantityField.clear();

            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, ex.getMessage())
                        .showAndWait();
            }
        });

        // --- Selected stock labels ---
        Label selectedLabel = new Label("Selected: ");
        Label detailsLabel = new Label("Price | High | Low | Change");

        stockTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldStock, newStock) -> {

                    if (newStock != null) {
                        selectedLabel.setText("Selected: " + newStock.getSymbol());

                        detailsLabel.setText(
                                "Price: " + format(newStock.getSalesPrice())
                                        + " | High: " + format(newStock.getHighestPrice())
                                        + " | Low: " + format(newStock.getLowestPrice())
                                        + " | Change: " + format(newStock.getLatestPriceChange())
                        );
                    }
                }
        );

        selectedBox.getChildren().addAll(
                selectedTitleRow,
                new Separator(),
                selectedLabel,
                detailsLabel,
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

        transactionTable = new TableView<>();

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

    private void showStartDialog(Stage owner) {

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Start New Game");

        TextField nameField = new TextField();
        TextField moneyField = new TextField();

        nameField.setPromptText("Enter name");
        moneyField.setPromptText("Enter starting money");

        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Money:"), moneyField
        );

        dialog.getDialogPane().setContent(content);

        ButtonType startsButton = new ButtonType("Start", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(startsButton);

        dialog.initOwner(owner);

        dialog.setResultConverter(button -> {
            if (button == startsButton) {

                String name = nameField.getText();

                if (!name.matches("[a-zA-Z ]+")) {
                    new Alert(Alert.AlertType.ERROR, "Name must contain only letters!")
                            .showAndWait();
                    return null;
                }

                try {
                    BigDecimal money = new BigDecimal(moneyField.getText());

                    controller.startNewGame(name, money, loadStocksAsMap());
                    stockTable.getItems().clear();
                    stockTable.getItems().addAll(controller.getExchange().findStocks(""));

                    buyButton.setDisable(false);
                    sellButton.setDisable(false);
                    nextWeekButton.setDisable(false);

                    updatePlayerInfo();
                    updatePortfolioTable();
                    updateTransactionTable();

                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Invalid input for money!").showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }


    private Map<String, Stock> loadStocksAsMap() {
        Map<String, Stock> map = new HashMap<>();

        for (Stock stock : loadStocksFromCSV()) {
            map.put(stock.getSymbol(), stock);
        }

        return map;
    }

    private List<Stock> loadStocksFromCSV() {
        List<Stock> stocks = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/Stocks.csv"))
        )) {

            String line;
            while ((line = reader.readLine()) != null) {

                // skip comments and empty lines
                if (line.startsWith("#") || line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length < 3) {
                    continue;
                }

                String symbol = parts[0];
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);

                Stock stock = new Stock(name, symbol, new BigDecimal(price));
                stocks.add(stock);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stocks;
    }

    private void updatePlayerInfo() {
        if (controller.getPlayer() == null) return;

        nameLabel.setText("Name: " + controller.getPlayer().getName());
        moneyLabel.setText("Money: " + controller.getPlayer().getMoney());
        netWorthLabel.setText("Net Worth: " + controller.getPlayer().getNetWorth());
        weekLabel.setText("Week: " + controller.getExchange().getWeek());

        int weeksPlayed = controller.getExchange().getWeek();
        statusLabel.setText("Status: " + controller.getPlayer().getStatus(weeksPlayed));
    }

    private void updatePortfolioTable() {
        if (controller.getPlayer() == null) return;

        portfolioTable.getItems().clear();
        portfolioTable.getItems().addAll(
                controller.getPlayer().getPortfolio().getAllShares()
        );
    }

    private void updateTransactionTable() {
        if (controller.getPlayer() == null) return;

        transactionTable.getItems().clear();
        transactionTable.getItems().addAll(
                controller.getPlayer().getArchive().getTransactions()
        );
    }

    private String format(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}