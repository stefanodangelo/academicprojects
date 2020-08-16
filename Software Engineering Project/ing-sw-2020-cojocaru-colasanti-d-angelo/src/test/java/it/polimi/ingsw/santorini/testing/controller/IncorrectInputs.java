package it.polimi.ingsw.santorini.testing.controller;

import it.polimi.ingsw.santorini.model.exceptions.PrintableRuntimeException;

public class IncorrectInputs extends PrintableRuntimeException {
    public IncorrectInputs() {
        super("Incorrect test inputs! None of them has been accepted");
    }
}
