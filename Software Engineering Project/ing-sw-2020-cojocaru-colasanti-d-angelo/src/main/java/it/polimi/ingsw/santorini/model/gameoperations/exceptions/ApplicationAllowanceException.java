package it.polimi.ingsw.santorini.model.gameoperations.exceptions;

import it.polimi.ingsw.santorini.model.exceptions.PrintableRuntimeException;

/**
 * Brief Exception thrown when some invalid applications are made
 */
public class ApplicationAllowanceException extends PrintableRuntimeException {
    public ApplicationAllowanceException(Boolean requiresApplication) {
        super(requiresApplication ?
                "Application task invoked on a not supporting application Expandable" :
                "Application is allowed, but it should not be for intended usage"
        );
    }
}
