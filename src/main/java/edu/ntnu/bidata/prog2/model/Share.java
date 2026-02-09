package edu.ntnu.bidata.prog2.model;

import java.math.BigDecimal;

public class Share {
    private Stock stock;
    private BigDecimal quantity;
    private BigDecimal purchasePrice;

    public Share(Stock stock, BigDecimal quantity, BigDecimal purchasePrice) {
        this.stock = stock;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
    }
}
