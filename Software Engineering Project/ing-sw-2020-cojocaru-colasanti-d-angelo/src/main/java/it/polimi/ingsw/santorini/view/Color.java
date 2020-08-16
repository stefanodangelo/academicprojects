package it.polimi.ingsw.santorini.view;

/**
 * Brief Enum containing the workers' colors
 */
public enum Color {
    YELLOW("\u001B[33;1m", "YELLOW"),
    RED("\u001B[31;1m", "RED"),
    CYAN("\u001B[36m", "CYAN"),
    GREEN("\u001B[32;1m", "GREEN"),
    BLUE("\u001B[94;1m", "BLUE"),
    WHITE("\u001B[37;1m", "WHITE"),
    REVERSED("\u001B[7m", "REVERSED"),
    BACKGROUND_BLUE("\u001B[44m", "BACKGROUND_BLUE"),
    BACKGROUND_GREEN("\u001B[42m", "BACKGROUND_GREEN"),
    BACKGROUND_YELLOW("\u001B[43m", "BACKGROUND_YELLOW"),
    BACKGROUND_CYAN("\u001B[46m", "BACKGROUND_CYAN");

    public static final String RESET = "\u001B[0m";
    private final String color;
    private final String name;

    Color(String color, String name) {
        this.color = color;
        this.name = name;
    }

    public String color() {
        return color;
    }
    public String getName() { return name; }

    /**
     * Brief Changes a message's formatting to another one in which it gets colored
     * @param message is the message to format
     * @param color is the color the message will get
     * @return the formatted message
     */
    public static String formatMessageColor(String message, Color color){
        return color.color() + message + Color.RESET;
    }
}
