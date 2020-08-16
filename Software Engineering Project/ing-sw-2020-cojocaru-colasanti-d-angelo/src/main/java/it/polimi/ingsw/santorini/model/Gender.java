package it.polimi.ingsw.santorini.model;

public enum Gender {
    MALE("\u25B2"),
    FEMALE("\u25B3");

    private final String symbol;

    Gender(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }
}
