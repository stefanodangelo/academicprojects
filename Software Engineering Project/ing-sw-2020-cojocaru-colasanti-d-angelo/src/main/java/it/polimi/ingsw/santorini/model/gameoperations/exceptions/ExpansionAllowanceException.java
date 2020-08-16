package it.polimi.ingsw.santorini.model.gameoperations.exceptions;

import it.polimi.ingsw.santorini.model.exceptions.PrintableRuntimeException;

/**
 * Brief Exception thrown when some invalid expansions are made
 */
public class ExpansionAllowanceException extends PrintableRuntimeException {
    public ExpansionAllowanceException(Boolean requiresExpansion) {
        super(requiresExpansion ?
                "Expansion task invoked on a not supporting expansion Expandable":
                "Expansion is allowed, but it should not be for intended usage"
        );
    }
}
