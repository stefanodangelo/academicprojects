package it.polimi.ingsw.santorini.view.modelimport;

/**
 * Brief Resumes the blocks' levels deriving from the model
 */
public enum BlockLevel {
    GROUND(" ","GROUND","WHITE"),
    LEVEL1("\u2610","LEVEL1","WHITE"),
    LEVEL2("\u25A3","LEVEL2","WHITE"),
    LEVEL3("\u2B21","LEVEL3","WHITE"),
    DOME  ("\u2B24","DOME  ","BLUE");

    private final String symbol;
    private final String name;
    private final String color;

    BlockLevel(String symbol, String name, String color) {
        this.symbol = symbol;
        this.name = name;
        this.color = color;
    }

    public String symbol() {
        return symbol;
    }
    public String getName() { return name;}
    public String color() { return color;}
}
