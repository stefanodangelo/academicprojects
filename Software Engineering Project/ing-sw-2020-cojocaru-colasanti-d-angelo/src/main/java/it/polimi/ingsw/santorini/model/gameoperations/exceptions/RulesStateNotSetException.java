package it.polimi.ingsw.santorini.model.gameoperations.exceptions;


import it.polimi.ingsw.santorini.model.exceptions.PrintableRuntimeException;

/**
 * Brief PrintableRuntimeException thrown when the Rules's state in not yet set
 * @see it.polimi.ingsw.santorini.model.gameoperations.rules.Rules
 */
public class RulesStateNotSetException extends PrintableRuntimeException {
    public RulesStateNotSetException() {
        super("Rules' state is not set yet!");
    }
}
