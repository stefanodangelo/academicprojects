package it.polimi.ingsw.santorini.model.exceptions;

import it.polimi.ingsw.santorini.model.utils.Printable;

/**
 * Brief Abstract exception containing a printable message to print when it is thrown
 */
public abstract class PrintableRuntimeException extends RuntimeException implements Printable {

    public PrintableRuntimeException(String message) {
        print(message);
    }

    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
