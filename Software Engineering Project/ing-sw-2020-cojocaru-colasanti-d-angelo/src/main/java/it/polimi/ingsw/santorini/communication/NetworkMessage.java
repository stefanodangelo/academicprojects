package it.polimi.ingsw.santorini.communication;

/**
 * Brief Enum for the print of a specific message on client side
 */
public enum NetworkMessage {
    startRefusal("There are not enough players to start a game"),
    startConfirmation("Game is starting..."),
    badInput("Not valid input."),
    host("You are the host"),
    sizeChanged("Changed lobby size"),
    sizeNotChanged("Can't change lobby size"),
    waitStart("Wait for the host to start a new game"),
    demandInput("Enter one of the listed commands"),
    waitVote("Wait until the other players finish to vote"),
    waitTurn("Wait until it is your turn"),
    waitChoice("Wait until the other players finish to make their choice");

    private final String message;

    NetworkMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    /**
     * @param id is the position in the enum of the wanted value
     * @return the NetworkMessage at the id-th position of the enum's array
     *
     */
    public static NetworkMessage byId(Integer id) {
        return NetworkMessage.values()[id];
    }
}
