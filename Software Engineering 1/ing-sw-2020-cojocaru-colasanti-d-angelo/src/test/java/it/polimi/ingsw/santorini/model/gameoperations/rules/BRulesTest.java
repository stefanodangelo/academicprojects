package it.polimi.ingsw.santorini.model.gameoperations.rules;

import it.polimi.ingsw.santorini.model.*;
import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.ConsumerExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.ListFunctionExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.PredicateExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.state.BState;
import it.polimi.ingsw.santorini.model.gameoperations.state.GameState;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BRulesTest {

    @Test
    void isCompleteTest() {
        MRules rules = new MRules(false);
        assertFalse(rules.isComplete(), "isCompleteTest failed");

        rules = new MRules();
        assertTrue(rules.isComplete(), "isCompleteTest failed");
    }

    @Test
    void completeRulesTest() {
        BRules rules = new BRules(false);
        rules.completeRules();
        assertTrue(rules.isComplete(), "isCompleteTest failed");
    }

    @Test
    void allowedPositionsTest() {
        BRules rules = new BRules(false);
        rules.setState(new BState());
        rules.getState().setGameState(new GameState(null, null, null));
        Worker worker = new Worker(null, null);
        worker.setPosition(new Position(3, 3));
        rules.getState().gameState().setActiveWorker(worker);
        rules.setFilter(GameMarker::equalsDefault);

        rules.allowedPositions().addExpansion(new PredicateExpansion<>(
                state -> state.chosenPosition().equals(new Position(3,4)),
                true, GameMarker.defaultMarker()
        ));
        rules.allowedPositions().addExpansion(new PredicateExpansion<>(
                state -> state.chosenPosition().equals(new Position(2,3)),
                true, GameMarker.defaultMarker()
        ));
        rules.allowedPositions().addExpansion(new PredicateExpansion<>(
                state -> state.chosenPosition().equals(new Position(1,1)),
                true, GameMarker.defaultMarker()
        ));
        rules.allowedPositions().addExpansion(new PredicateExpansion<>(
                state -> !state.chosenPosition().equals(new Position(2,3)),
                false, GameMarker.defaultMarker()
        ));
        rules.allowedPositions().addExpansion(new PredicateExpansion<>(
                state -> state.chosenPosition().equals(new Position(3,3)),
                true, GameMarker.defaultMarker()
        ));

        rules.setPermissions(false, true);
        List<Position> positions = rules.applyAllowedPositions();
        assertTrue(positions.containsAll(new ArrayList<>(Arrays.asList(new Position(3, 4), new Position(3, 3)))),
                "allowedPositionsTest failed");
    }

    @Test
    void allowedBlockTypesTest() {
        BRules rules = new BRules(false);
        rules.setState(new BState());
        rules.setFilter(GameMarker::equalsDefault);

        rules.allowedBlockTypes().addExpansion(new ListFunctionExpansion<>(
                (bState) -> new ArrayList<>(Arrays.asList(BlockType.LEVEL1, BlockType.DOME)),
                true, GameMarker.defaultMarker()
        ));
        rules.allowedBlockTypes().addExpansion(new ListFunctionExpansion<>(
                (bState) -> new ArrayList<>(Collections.singletonList(BlockType.DOME)),
                false, GameMarker.defaultMarker()
        ));
        rules.allowedBlockTypes().addExpansion(new ListFunctionExpansion<>(
                (bState) -> new ArrayList<>(Arrays.asList(BlockType.LEVEL1, BlockType.LEVEL2, BlockType.LEVEL3)),
                true, GameMarker.defaultMarker()
        ));

        rules.setPermissions(false, true);
        List<BlockType> blockTypes = rules.applyAllowedBlockTypes();
        assertTrue(blockTypes.containsAll(new ArrayList<>(Arrays.asList(BlockType.LEVEL1, BlockType.LEVEL2, BlockType.LEVEL2))),
                "allowedBlockTypesTest failed");
    }

    @Test
    void buildingTest() {
        BRules rules = new BRules(false);
        rules.setState(new BState());
        rules.setFilter(GameMarker::equalsDefault);

        rules.getState().setGameState(
                new GameState(
                        new GameMap(),
                        null, null));

        rules.getState().setChosenPosition(new Position(0, 4));
        rules.getState().setChosenBlockType(BlockType.LEVEL1);

        rules.building().addExpansion(new ConsumerExpansion<>(
                (bState) -> {
                    bState.map().addGameObject(new Block(bState.getChosenBlockType()), bState.chosenPosition());
                }, GameMarker.defaultMarker()
        ));

        rules.setPermissions(false, true);
        rules.applyBuilding();
        assertEquals(1, (int) rules.getState().map().cellAt(new Position(0, 4)).getLevel(),
                "buildingTest failed");
    }
}