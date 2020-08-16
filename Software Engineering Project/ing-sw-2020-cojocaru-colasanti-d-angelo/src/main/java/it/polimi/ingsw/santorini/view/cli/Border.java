package it.polimi.ingsw.santorini.view.cli;

/**
 * Brief Enumeration of the various types of borders for the CLI printing
 */
public enum Border {
    HORIZONTAL_BORDER("\u2501"),
    VERTICAL_BORDER("\u2503"),
    LEFT_CROSSED_BORDER("\u2523"),
    RIGHT_CROSSED_BORDER("\u252B"),
    UPPER_LEFT_CORNER("\u250F"),
    UPPER_RIGHT_CORNER("\u2513"),
    LOWER_LEFT_CORNER("\u2517"),
    LOWER_RIGHT_CORNER("\u251B"),
    DOUBLE_HORIZ_BORDER("\u2550"),
    CROSSED_BORDERS("\u256C"),
    DOUBLE_LEFT_CROSSED_BORDER("\u2560"),
    DOUBLE_RIGHT_CROSSED_BORDER("\u2563"),
    UP_CROSSED_BORDER("\u2566"),
    DOWN_CROSSED_BORDER("\u2569"),
    DOUBLE_UPPER_LEFT_CORNER("\u2554"),
    DOUBLE_UPPER_RIGHT_CORNER("\u2557"),
    DOUBLE_LOWER_LEFT_CORNER("\u255A"),
    DOUBLE_LOWER_RIGHT_CORNER("\u255D"),
    CENTRAL_BORDER("\u2551");

    private final String symbol;

    Border(String symbol){
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }
}
