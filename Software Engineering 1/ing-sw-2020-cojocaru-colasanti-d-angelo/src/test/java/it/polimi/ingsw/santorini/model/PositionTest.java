package it.polimi.ingsw.santorini.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PositionTest {
    Position position;

    @BeforeEach
    void init(){
        position = new Position(0,0);
    }

    @Test
    void getXTest() {
        assertNotNull(position.getX(),"Position getX() returned null!");
        assertEquals(0,position.getX(), "Position getX() returned a different intValueOf than the initializer one!");
    }

    @Test
    void setXTest() {
        position.setX(1);
        assertNotNull(position.getX(),"Position getX() returned null!");
        assertEquals(1,position.getX(), "Position getX() returned a different intValueOf than the set one!");
    }

    @Test
    void getYTest() {
        assertNotNull(position.getY(), "Position getY() returned null!");
        assertEquals(0,position.getY(), "Position getY() returned a different intValueOf than the initializer one!");
    }

    @Test
    void setYTest() {
        position.setY(1);
        assertNotNull(position.getY(), "Position getY() returned null!");
        assertEquals(1,position.getY(), "Position getY() returned a different intValueOf than the set one!");
    }
}