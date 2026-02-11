package edu.ntnu.bidata.prog2.model;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    private final List<Share> shares;

    public Portfolio() {
        this.shares = new ArrayList<>();
    }

    //to fix
    // getShares(symbol:String): List<Share>

    public List<Share> getShares() {
        return shares;
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
}
