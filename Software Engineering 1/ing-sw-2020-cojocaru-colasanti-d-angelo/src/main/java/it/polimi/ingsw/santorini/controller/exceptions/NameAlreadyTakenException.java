package it.polimi.ingsw.santorini.controller.exceptions;

/**
 * Brief Exception thrown when a player chooses a name that has already been taken by another player
 */
public class NameAlreadyTakenException extends RuntimeException {
    public String getMessage(){
        return "Name already taken. Choose another one.";
    }
}
