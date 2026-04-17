package edu.ntnu.bidata.prog2.ui.view;

import edu.ntnu.bidata.prog2.model.Share;
import edu.ntnu.bidata.prog2.model.Stock;
import edu.ntnu.bidata.prog2.observer.GameEvent;
import edu.ntnu.bidata.prog2.observer.GameObserver;
import edu.ntnu.bidata.prog2.transaction.Transaction;
import edu.ntnu.bidata.prog2.ui.controller.WindowViewController;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Map;

/**
 * The main window view for the Millions stock market game.
 * Implements GameObserver so it automatically refreshes when the model changes.
 * Contains no business logic — all data preparation is done in the controller.
 */
public class WindowView extends Application implements GameObserver {
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
    private Label selectedLabel;
    private Label detailsLabel;
    private Label gainersLabel;
    private Label losersLabel;

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
        left.setPrefWidth(220);
        left.setStyle("-fx-padding: 15;");

        nameLabel = new Label("Name: ");
        moneyLabel = new Label("Money: ");
        netWorthLabel = new Label("Net Worth: ");
        statusLabel = new Label("Status: ");
        weekLabel = new Label("Week: ");

        Button startButton = new Button("New Game");
        startButton.setMaxWidth(Double.MAX_VALUE);

        startButton.setOnAction(e -> showStartDialog(stage));

        // MARKET MOVERS BOX (now on the left, below the player info)
        VBox moversBox = new VBox(10);

        HBox moversTitleRow = new HBox(10);
        moversTitleRow.getChildren().addAll(
                new Label("📈"),
                new Label("Market Movers")
        );

        gainersLabel = new Label("Top Gainers:\n");
        gainersLabel.setStyle("-fx-text-fill: green;");

        losersLabel = new Label("Top Losers:\n");
        losersLabel.setStyle("-fx-text-fill: red;");

        moversBox.getChildren().addAll(
                moversTitleRow,
                new Separator(),
                gainersLabel,
                losersLabel
        );

        left.getChildren().addAll(
                nameLabel, moneyLabel, netWorthLabel, statusLabel, weekLabel, startButton,
                new Separator(),
                moversBox
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

        stockTable.getColumns().add(symbolCol);
        stockTable.getColumns().add(priceCol);
        stockTable.getColumns().add(changeCol);

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            stockTable.getItems().clear();
            stockTable.getItems().addAll(
                    controller.searchStocks(newValue)
            );
        });

        stockTable.setPrefSize(250, 245);
        stockTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

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
                new SimpleStringProperty(controller.getShareValue(data.getValue())));

        portfolioTable.getColumns().add(pSymbolCol);
        portfolioTable.getColumns().add(qtyCol);
        portfolioTable.getColumns().add(valueCol);

        portfolioTable.setPrefSize(250, 250);
        portfolioTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        portfolioBox.getChildren().addAll(
                portfolioTitleRow,
                new Separator(),
                new Label("Owned Shares"),
                portfolioTable
        );

        // MAIN CONTENT (TOP ROW) — back to 2 columns, movers is on the left
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

        // next week button action — no manual refresh (observer does it)
        nextWeekButton.setOnAction(e -> {
            try {
                controller.nextWeek();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });

        // buy button — shows preview, then executes, then shows receipt
        buyButton.setOnAction(e -> {
            try {
                Stock selectedStock = stockTable.getSelectionModel().getSelectedItem();
                String quantity = quantityField.getText().trim();

                if (selectedStock == null) {
                    throw new IllegalArgumentException("Select a stock first!");
                }
                if (quantity.isBlank()) {
                    throw new IllegalArgumentException("Quantity cannot be empty!");
                }

                String preview = controller.previewPurchase(selectedStock, quantity);
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, preview,
                        ButtonType.OK, ButtonType.CANCEL);
                confirm.setTitle("Confirm Purchase");
                confirm.setHeaderText("Review your purchase");

                if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
                    return;
                }

                controller.buy(selectedStock, quantity);
                quantityField.clear();

                Alert receipt = new Alert(Alert.AlertType.INFORMATION,
                        controller.getLastTransactionReceipt());
                receipt.setTitle("Purchase Complete");
                receipt.setHeaderText("Transaction successful");
                receipt.showAndWait();

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });

        // sell button — shows preview, then executes, then shows receipt
        sellButton.setOnAction(e -> {
            try {
                Share selectedShare = portfolioTable.getSelectionModel().getSelectedItem();
                String quantity = quantityField.getText().trim();

                if (selectedShare == null) {
                    throw new IllegalArgumentException("Select a share first!");
                }
                if (quantity.isBlank()) {
                    throw new IllegalArgumentException("Quantity cannot be empty!");
                }

                String preview = controller.previewSale(selectedShare, quantity);
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, preview,
                        ButtonType.OK, ButtonType.CANCEL);
                confirm.setTitle("Confirm Sale");
                confirm.setHeaderText("Review your sale");

                if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
                    return;
                }

                controller.sell(selectedShare, quantity);
                quantityField.clear();

                Alert receipt = new Alert(Alert.AlertType.INFORMATION,
                        controller.getLastTransactionReceipt());
                receipt.setTitle("Sale Complete");
                receipt.setHeaderText("Transaction successful");
                receipt.showAndWait();

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });

        // --- Selected stock labels ---
        selectedLabel = new Label("Selected: ");
        detailsLabel = new Label("Price | High | Low | Change");

        stockTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldStock, newStock) -> {
                    if (newStock != null) {
                        selectedLabel.setText("Selected: " + newStock.getSymbol());
                        detailsLabel.setText(controller.getStockDetails(newStock));
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

        transactionTable.getColumns().add(weekCol);
        transactionTable.getColumns().add(typeCol);
        transactionTable.getColumns().add(stockCol);
        transactionTable.getColumns().add(qtyColX);
        transactionTable.getColumns().add(totalCol);

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

    /**
     * Shows the "Start New Game" dialog where the user enters their name, starting money,
     * and selects a stock data file from anywhere on the computer.
     *
     * @param owner the owner stage for the dialog
     */
    private void showStartDialog(Stage owner) {

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Start New Game");
        dialog.initOwner(owner);

        TextField nameField = new TextField();
        TextField moneyField = new TextField();
        TextField fileField = new TextField();

        nameField.setPromptText("Enter name");
        moneyField.setPromptText("Enter starting money");
        fileField.setPromptText("No file selected");
        fileField.setEditable(false);
        fileField.setPrefWidth(300);

        Button browseButton = new Button("Browse...");
        browseButton.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Stock Data File");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

            File file = chooser.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
            if (file != null) {
                fileField.setText(file.getAbsolutePath());
            }
        });

        HBox fileRow = new HBox(10, fileField, browseButton);

        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Money:"), moneyField,
                new Label("Stock data file:"), fileRow
        );

        dialog.getDialogPane().setContent(content);

        ButtonType startsButton = new ButtonType("Start", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(startsButton, cancelButton);

        dialog.setResultConverter(button -> {
            if (button == startsButton) {
                try {
                    controller.startNewGame(
                            nameField.getText(),
                            moneyField.getText(),
                            fileField.getText()
                    );

                    controller.addGameObserver(this);

                    buyButton.setDisable(false);
                    sellButton.setDisable(false);
                    nextWeekButton.setDisable(false);

                    onGameChanged(GameEvent.GAME_STARTED);

                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    /**
     * Called by the model (Exchange) whenever the game state changes.
     * This is the central place where the UI is refreshed.
     *
     * @param event the type of change that occurred
     */
    @Override
    public void onGameChanged(GameEvent event) {
        switch (event) {
            case GAME_STARTED -> {
                stockTable.getItems().clear();
                stockTable.getItems().addAll(controller.searchStocks(""));
                updatePlayerInfo();
                updatePortfolioTable();
                updateTransactionTable();
                updateMarketMovers();
            }
            case TRANSACTION_COMPLETED -> {
                updatePlayerInfo();
                updatePortfolioTable();
                updateTransactionTable();
                stockTable.refresh();
                refreshSelectedStockDetails();
            }
            case WEEK_ADVANCED -> {
                updatePlayerInfo();
                updatePortfolioTable();
                stockTable.refresh();
                refreshSelectedStockDetails();
                updateMarketMovers();
            }
        }
    }

    private void updatePlayerInfo() {

        Map<String, String> info = controller.getPlayerInfo();

        nameLabel.setText("Name: " + info.get("name"));
        moneyLabel.setText("Money: " + info.get("money"));
        netWorthLabel.setText("Net Worth: " + info.get("netWorth"));
        statusLabel.setText("Status: " + info.get("status"));
        weekLabel.setText("Week: " + info.get("week"));
    }

    private void updatePortfolioTable() {
        portfolioTable.getItems().clear();
        portfolioTable.getItems().addAll(
                controller.getPortfolioData()
        );
    }

    private void updateTransactionTable() {
        transactionTable.getItems().clear();
        transactionTable.getItems().addAll(
                controller.getTransactionData()
        );
    }

    /**
     * Refreshes the "Selected Stock" details label using current model data.
     * Called by the observer so that prices, high/low, and change stay up-to-date
     * even when the user doesn't re-click the stock.
     */
    private void refreshSelectedStockDetails() {
        Stock selected = stockTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            detailsLabel.setText(controller.getStockDetails(selected));
        }
    }

    /**
     * Refreshes the Market Movers panel with the current top gainers and losers.
     */
    private void updateMarketMovers() {
        StringBuilder gainers = new StringBuilder("Top Gainers:\n");
        for (Stock stock : controller.getGainers(3)) {
            gainers.append(controller.formatMarketMover(stock)).append("\n");
        }
        gainersLabel.setText(gainers.toString());

        StringBuilder losers = new StringBuilder("Top Losers:\n");
        for (Stock stock : controller.getLosers(3)) {
            losers.append(controller.formatMarketMover(stock)).append("\n");
        }
        losersLabel.setText(losers.toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}