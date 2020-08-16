package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.model.exceptions.EmptyCellException;
import it.polimi.ingsw.santorini.view.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    Cell cellTest;

    @BeforeEach
    void init(){
        cellTest = new Cell();
    }

    @Test
    void NullTests() {
        assertAll( () -> assertNotNull(cellTest.getLevel(), "Stack should be initialized!"),
                   () -> assertNull(cellTest.getOccupantId(), "occupantId should be null at the creation of the class")
                 );
    }

    @Test
    void getCurrentLevelTest() {
        cellTest.pushGameObject(new Block(BlockType.LEVEL1));
        cellTest.pushGameObject(new Block(BlockType.LEVEL2));
        assertEquals(2, cellTest.getLevel(), "should return the correct level");
    }

    @Test
    void pushGameObjectTest1() {
        Worker worker = new Worker(Color.YELLOW.getName(), Gender.MALE);
        cellTest.pushGameObject(worker);
        assertAll( () -> assertTrue(cellTest.isOccupied(), "Cell should be occupied"),
                   () -> assertEquals(worker.occupantId(), cellTest.getOccupantId(), "Cell's occupantId should be correct"),
                   () -> assertEquals(worker, cellTest.popGameObject(), "Pop on Cell's stack should return worker correctly"),
                   () -> assertFalse(cellTest.isOccupied(), "Cell shouldn't be occupied"),
                   () -> assertNull(cellTest.getOccupantId(), "Cell's occupantId should be null")
                 );
    }

    @Test
    void pushGameObjectTest2 (){
        cellTest.pushGameObject(new Block(BlockType.DOME));
        assertTrue(cellTest.isBlocked(), "Cell should be blocked by a Dome");
    }

    @Test
    void pushGameObjectTest3 (){
        assertTrue(cellTest.isReachable(), "Cell shouldn't be occupied by a worker");
        cellTest.pushGameObject(new Worker(Color.YELLOW.getName(), Gender.FEMALE));
        assertTrue(cellTest.isOccupied(), "Cell should be occupied by a worker");
    }

    @Test
    void popGameObjectTest1(){
        assertThrows(EmptyCellException.class, () -> cellTest.popGameObject(), "There should be nothing to pop from the Stack");
        Worker worker = new Worker (Color.YELLOW.getName(), Gender.MALE);
        cellTest.pushGameObject(worker);
        assertEquals(worker, cellTest.popGameObject(), "should pop the correct GameObject");
        assertFalse(cellTest.isOccupied());
        Block dome = new Block (BlockType.DOME);
        cellTest.pushGameObject(dome);
        assertTrue(cellTest.isBlocked());
        assertEquals(dome, cellTest.popGameObject(), "should pop the correct Block");
    }

    @Test
    void popGameObjectTest2() {
        cellTest.pushGameObject(new Block(BlockType.LEVEL1));
        cellTest.pushGameObject(new Block(BlockType.LEVEL2));
        cellTest.pushGameObject(new Block(BlockType.LEVEL3));
        assertEquals(3, cellTest.getLevel(), "should return the correct current level");
        cellTest.popGameObject();
        assertEquals(2, cellTest.getLevel(), "should return the correct current level");
        cellTest.popGameObject();
        assertEquals(1, cellTest.getLevel(), "should return the correct current level");
        cellTest.popGameObject();
        assertEquals(0, cellTest.getLevel(),"should return the correct current level");
        assertThrows(EmptyCellException.class, () -> cellTest.popGameObject(), "There should be nothing to pop from the Stack");
    }

    @Test
    void getOccupantIdTest1(){
        Worker worker = new Worker(Color.YELLOW.getName(), Gender.FEMALE);
        worker.setPlayerId(5);
        cellTest.pushGameObject(worker);
        assertEquals(5, cellTest.getOccupantId(), "should return the correct occupantId");
        cellTest.popGameObject();
        assertNull(cellTest.getOccupantId(),"should return null occupantId");
    }

    @Test
    void getOccupantIdTest2(){
        cellTest.pushGameObject(new Block(BlockType.DOME));
        assertNull(cellTest.getOccupantId(), "should return null occupantId");
    }
}