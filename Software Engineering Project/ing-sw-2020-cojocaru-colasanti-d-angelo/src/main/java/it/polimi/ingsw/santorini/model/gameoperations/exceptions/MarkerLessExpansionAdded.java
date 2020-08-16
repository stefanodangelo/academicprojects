package it.polimi.ingsw.santorini.model.gameoperations.exceptions;

import it.polimi.ingsw.santorini.model.exceptions.PrintableRuntimeException;

/**
 * Brief PrintableRuntimeException thrown when an Expansion without markers is added to an Expandable
 * @see it.polimi.ingsw.santorini.model.gameoperations.expansion.Expandable
 * @see it.polimi.ingsw.santorini.model.gameoperations.expansion.Expansion
 */
public class MarkerLessExpansionAdded extends PrintableRuntimeException {
    public MarkerLessExpansionAdded() {
        super("An expansion with empty markers list added to Expandable that requires at least one marker per expansion!");
    }
}
