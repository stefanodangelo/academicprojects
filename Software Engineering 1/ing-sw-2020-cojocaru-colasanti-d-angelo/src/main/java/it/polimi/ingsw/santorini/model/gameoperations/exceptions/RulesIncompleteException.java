package it.polimi.ingsw.santorini.model.gameoperations.exceptions;

import it.polimi.ingsw.santorini.model.exceptions.PrintableRuntimeException;

/**
 * Brief PrintableRuntimeException thrown when Rules that are being used are not complete
 * @see it.polimi.ingsw.santorini.model.gameoperations.GameOperation
 */
public class RulesIncompleteException extends PrintableRuntimeException {
    public RulesIncompleteException() {
        super("Rules are incomplete!");
    }
}
