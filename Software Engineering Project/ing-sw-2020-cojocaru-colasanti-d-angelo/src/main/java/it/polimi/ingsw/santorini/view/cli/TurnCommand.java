package it.polimi.ingsw.santorini.view.cli;

import it.polimi.ingsw.santorini.communication.ImmutablePosition;

import java.util.Objects;

/**
 * Brief Enumeration for the available commands during a whole turn (Move+Build)
 */
public enum TurnCommand {
    NORTHWEST("q", "\u2196"),
    NORTH("w", "\u2191"),
    NORTHEAST("e", "\u2197"),
    WEST("a","\u2190"),
    SAME("s", null),
    EAST("d", "\u2192"),
    SOUTHWEST("z", "\u2199"),
    SOUTH("x", "\u2193"),
    SOUTHEAST("c", "\u2198");

    private final String command;
    private final String symbol;
    private static final String header = "Move and Build commands:";

    TurnCommand(String command, String symbol){
        this.command = command;
        this.symbol = symbol;
    }

    public String getCommand() {
        return command;
    }
    public String getSymbol() { return symbol; }
    public static String getHeader() { return header; }

    /**
     * @param command corresponds to a certain keyboard button's value
     * @return the command represented by the value passed as a parameter to the function
     */
    public static TurnCommand getCommandByString(String command){
        TurnCommand[] values = TurnCommand.values();
        for (TurnCommand value : values)
            if (value.getCommand().equals(command))
                return value;
        return null;
    }

    /**
     * @param command is the pressed keyboard button's value
     * @param currentPosition is the position from which to calculate the new position
     * @return the position in the direction of the pressed keyboard button
     */
    public static ImmutablePosition getPositionByCommand(String command, ImmutablePosition currentPosition){
        TurnCommand kbCommand = getCommandByString(command);
        try {
            switch (Objects.requireNonNull(kbCommand)) {
                case NORTHWEST: return new ImmutablePosition(currentPosition.getX() - 1, currentPosition.getY() - 1);
                case NORTH:     return new ImmutablePosition(currentPosition.getX() - 1, currentPosition.getY());
                case NORTHEAST: return new ImmutablePosition(currentPosition.getX() - 1, currentPosition.getY() + 1);
                case WEST:      return new ImmutablePosition(currentPosition.getX(), currentPosition.getY() - 1);
                case SAME:      return currentPosition;
                case EAST:      return new ImmutablePosition(currentPosition.getX(), currentPosition.getY() + 1);
                case SOUTHWEST: return new ImmutablePosition(currentPosition.getX() + 1, currentPosition.getY() - 1);
                case SOUTH:     return new ImmutablePosition(currentPosition.getX() + 1, currentPosition.getY());
                case SOUTHEAST: return new ImmutablePosition(currentPosition.getX() + 1, currentPosition.getY() + 1);
                default:        return null;
            }
        } catch (NullPointerException e){
            return null;
        }
    }
}
