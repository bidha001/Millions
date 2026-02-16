package edu.ntnu.bidata.prog2.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    private final List<Share> shares;

    public Portfolio() {
        this.shares = new ArrayList<>();
    }

    public void addShare(Share share) {
        shares.add(share);
    }

    public void removeShare(Share share) {
        shares.remove(share);
    }

    public boolean containsShare(Share share) {
        return shares.contains(share);
    }

    public List<Share> getShares(String symbol) { //return all the shares in the portfolio that have the same stock symbol.
        List<Share> result = new ArrayList<>();

        for (Share share : shares) {
            if (share.getStock().getSymbol().equals(symbol)) {
                result.add(share);
            }
        }
        return result;
    }

    public BigDecimal getTotalQuantity(String symbol) {
        BigDecimal totalQuantity = BigDecimal.ZERO;

        for (Share share : shares) {
            if (share.getStock().getSymbol().equals(symbol)) {
                totalQuantity = totalQuantity.add(share.getQuantity());
            }
        }
        return totalQuantity;
    }
}
