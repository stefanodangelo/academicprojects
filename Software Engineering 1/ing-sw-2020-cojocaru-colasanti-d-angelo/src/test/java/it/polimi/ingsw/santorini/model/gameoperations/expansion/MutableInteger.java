package it.polimi.ingsw.santorini.model.gameoperations.expansion;

class MutableInteger {
    private int integer;

    MutableInteger(int value) {
        integer = value;
    }

    void plus(int value) {
        integer += value;
    }

    void minus(int value) {
        integer -= value;
    }

    int get() {
        return integer;
    }

    void set(int value) {
        integer = value;
    }
}
