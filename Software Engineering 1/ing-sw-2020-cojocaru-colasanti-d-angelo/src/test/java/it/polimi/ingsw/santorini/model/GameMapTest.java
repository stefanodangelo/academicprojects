package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.model.exceptions.IncorrectBlockTypeException;
import it.polimi.ingsw.santorini.model.exceptions.OutOfBoundsException;
import it.polimi.ingsw.santorini.model.exceptions.SamePositionException;
import it.polimi.ingsw.santorini.view.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {
    GameMap map;
    Cell[][] board;

    @BeforeEach
    void init() {
        board = new Cell[GameMap.getDefaultHeight()][GameMap.getDefaultWidth()];
        map = new GameMap();
    }

    @Test
    void moveTest1() {
        Worker worker = new Worker(Color.YELLOW.getName(), Gender.MALE);
        Position position1 = new Position(4,1);
        Position position2 = new Position(3,0);
        map.addGameObject(worker, position1);
        assertTrue(map.move(position1, position2), "move function should work correctly");
        assertEquals(position2, worker.getPosition(), "should return the correct position");
        assertFalse(map.move(position1, position2), "move function shouldn't work correctly");
    }

    @Test
    void moveTest2() {
        Worker worker = new Worker(Color.YELLOW.getName(), Gender.MALE);
        Position position1 = new Position(4,1);
        map.addGameObject(worker, position1);
        assertThrows(OutOfBoundsException.class, () -> map.move(position1, new Position(7,0)), "should throw OutOfBoundsException");
        assertThrows(SamePositionException.class, () -> map.move(position1, position1), "should throw SamePositionException");
    }

    @Test
    void addGameObjectTest() {
        Position position = new Position(3,2);
        map.addGameObject(new Block(1), position);
        assertThrows(IncorrectBlockTypeException.class, () -> map.addGameObject(new Block(BlockType.LEVEL3), position), "should throw IncorrectBlockTypeException");
        assertThrows(IncorrectBlockTypeException.class, () -> map.addGameObject(new Block(BlockType.LEVEL1), position), "should throw IncorrectBlockTypeException");
        map.addGameObject(new Block(2), position);
        assertThrows(IncorrectBlockTypeException.class, () -> map.addGameObject(new Block(BlockType.LEVEL1), position), "should throw IncorrectBlockTypeException");
        assertThrows(IncorrectBlockTypeException.class, () -> map.addGameObject(new Block(BlockType.LEVEL2), position), "should throw IncorrectBlockTypeException");
        map.addGameObject(new Block(3), position);
        assertThrows(IncorrectBlockTypeException.class, () -> map.addGameObject(new Block(BlockType.LEVEL1), position), "should throw IncorrectBlockTypeException");
        assertThrows(IncorrectBlockTypeException.class, () -> map.addGameObject(new Block(BlockType.LEVEL2), position), "should throw IncorrectBlockTypeException");
        assertThrows(IncorrectBlockTypeException.class, () -> map.addGameObject(new Block(BlockType.LEVEL3), position), "should throw IncorrectBlockTypeException");
    }

    @Test
    void getReachableTest1() {
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                Position position = new Position(i, j);
                if(i == 0 && j == 0) map.addGameObject(new Block(BlockType.DOME), position);
                else if(i == 0 && j == 1) map.addGameObject(new Worker(Color.YELLOW.getName(), Gender.FEMALE), position);
                else if(i == 0) {
                    map.addGameObject(new Block(BlockType.LEVEL1), position);
                    map.addGameObject(new Worker(Color.YELLOW.getName(), Gender.MALE), position);
                }
                else if(i == 1 && j == 0) {
                    map.addGameObject(new Block(BlockType.LEVEL1), position);
                    map.addGameObject(new Block(BlockType.LEVEL2), position);
                    map.addGameObject(new Block(BlockType.LEVEL3), position);
                }
                else if(i == 1 && j == 2) {
                    map.addGameObject(new Block(BlockType.LEVEL1), position);
                    map.addGameObject(new Block(BlockType.LEVEL2), position);
                }
                else if(i == 1) map.addGameObject((new Block(BlockType.LEVEL1)), position);
            }
        }
        ArrayList<Position> expectedNeighbors = new ArrayList<>();
        expectedNeighbors.add(new Position(1,2));
        expectedNeighbors.add(new Position(2,0));
        expectedNeighbors.add(new Position(2,1));
        assertTrue(map.getValidDestinations(new Position (1, 1)).containsAll(expectedNeighbors));
    }

    @Test
    void getReachableTest2() {
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 2; j++){
                Position position = new Position(i, j);
                if(i == 0 && j == 0) {
                    map.addGameObject(new Block(BlockType.LEVEL1), position);
                    map.addGameObject(new Worker(Color.YELLOW.getName(), Gender.FEMALE), position);
                }
                else if(i == 0) map.addGameObject(new Block(BlockType.DOME), position);
                else if(i == 1 && j == 0) map.addGameObject(new Block(BlockType.LEVEL1), position);
                else if(i == 1) {
                    map.addGameObject(new Block(BlockType.LEVEL1), position);
                    map.addGameObject(new Block(BlockType.LEVEL2), position);
                    map.addGameObject(new Block(BlockType.LEVEL3), position);
                }
                else if(j == 0) {
                    map.addGameObject((new Block(BlockType.LEVEL1)), position);
                    map.addGameObject((new Block(BlockType.LEVEL2)), position);
                }
                else map.addGameObject((new Block(BlockType.LEVEL1)), position);
                }
            }
        ArrayList<Position> expectedNeighbors = new ArrayList<>();
        expectedNeighbors.add(new Position(2,0));
        expectedNeighbors.add(new Position(2,1));
        assertAll(
                () -> assertTrue(map.getValidDestinations(new Position (1, 0)).containsAll(expectedNeighbors))
        );
    }

    @Test
    void buildNextLevelTest1(){
        Position position = new Position(2, 2);
        map.addGameObject(new Block(BlockType.DOME), position);
        assertFalse(map.buildNextLevel(position), () -> "Shouldn't build on a Cell with a dome on top");
        map.cellAt(position).popGameObject();
        assertTrue(map.buildNextLevel(position), () -> "Should return true");
        assertEquals(1, map.cellAt(position).getLevel(), () -> "Cell should be at level 1");
        assertTrue(map.buildNextLevel(position), () -> "Should return true");
        assertEquals(2, map.cellAt(position).getLevel(), () -> "Cell should be at level 2");
        assertTrue(map.buildNextLevel(position), () -> "Should return true");
        assertEquals(3, map.cellAt(position).getLevel(), () -> "Cell should be at level 3");
        assertTrue(map.buildNextLevel(position),() -> "Should return true");
        assertEquals(4, map.cellAt(position).getLevel(),  () -> "Cell should be at level 4");
        assertTrue(map.cellAt(position).isBlocked(),() -> "Cell should be blocked");
        assertFalse(map.buildNextLevel(position), () -> "Should return false");
    }

    @Test
    void buildNextLevelTest2(){
        Position position = new Position(2, 2);
        map.addGameObject(new Worker(Color.YELLOW.getName(), Gender.MALE), position);
        assertFalse(map.cellAt(position).isReachable());
        assertTrue(map.cellAt(position).isOccupied());
        assertFalse(map.cellAt(position).isBlocked());
        assertTrue(map.buildNextLevel(position), () -> "Should return true");
        assertEquals(1, map.cellAt(position).getLevel(), () -> "Cell should be at level 1");
        assertTrue(map.buildNextLevel(position), () -> "Should return true");
        assertEquals(2, map.cellAt(position).getLevel(), () -> "Cell should be at level 2");
        assertTrue(map.buildNextLevel(position), () -> "Should return true");
        assertEquals(3, map.cellAt(position).getLevel(), () -> "Cell should be at level 3");
        assertFalse(map.buildNextLevel(position),() -> "Should return false");
        assertFalse(map.cellAt(position).isReachable());
        assertTrue(map.cellAt(position).isOccupied());
        assertFalse(map.cellAt(position).isBlocked());
    }
}