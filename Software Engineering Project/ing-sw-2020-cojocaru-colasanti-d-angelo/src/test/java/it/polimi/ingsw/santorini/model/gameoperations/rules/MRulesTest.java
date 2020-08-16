package it.polimi.ingsw.santorini.model.gameoperations.rules;

import it.polimi.ingsw.santorini.model.*;
import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.ConsumerExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.ListFunctionExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.PredicateExpansion;
import it.polimi.ingsw.santorini.model.gameoperations.state.BState;
import it.polimi.ingsw.santorini.model.gameoperations.state.GameState;
import it.polimi.ingsw.santorini.model.gameoperations.state.MState;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MRulesTest {

    @Test
    void isCompleteTest() {
        MRules rules = new MRules(false);
        assertFalse(rules.isComplete(), "isCompleteTest failed");

        rules = new MRules();
        assertTrue(rules.isComplete(), "isCompleteTest failed");
    }

    @Test
    void completeRulesTest() {
        MRules rules = new MRules(false);
        rules.completeRules();
        assertTrue(rules.isComplete(), "isCompleteTest failed");
    }

    @Test
    void allowedPositionsTest() {
        MRules rules = new MRules(false);
        rules.setState(new MState());
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
    void winConditionTest() {
        MRules rules = new MRules(false);
        rules.setState(new MState());
        rules.setFilter(GameMarker::equalsDefault);

        rules.winCondition().addExpansion(new PredicateExpansion<>(
                (mState) -> true,
                true, GameMarker.defaultMarker()
        ));
        rules.winCondition().addExpansion(new PredicateExpansion<>(
                (mState) -> false,
                true, GameMarker.defaultMarker()
        ));
        rules.winCondition().addExpansion(new PredicateExpansion<>(
                (mState) -> false,
                false, GameMarker.defaultMarker()
        ));
        rules.winCondition().addExpansion(new PredicateExpansion<>(
                (mState) -> true,
                true, GameMarker.defaultMarker()
        ));

        rules.setPermissions(false, true);
        Boolean win = rules.applyWinCondition();
        assertFalse(win,"winConditionTest failed");
    }

    @Test
    void movementTest() {
        MRules rules = new MRules(false);
        rules.setState(new MState());
        rules.setFilter(GameMarker::equalsDefault);

        Player pl0 = new Player(0, "pl0");

        Worker w1 = new Worker(null, null);
        w1.setPosition(new Position(0,0));

        Worker w2 = new Worker(null, null);
        w2.setPosition(new Position(0,1));

        pl0.setWorkers(new ArrayList<>(Arrays.asList(w1, w2)));

        rules.getState().setGameState(
                new GameState(
                new GameMap(),
                new ArrayList<>(Arrays.asList(pl0, new Player(1, "pl1"))), null));

        rules.getState().map().addGameObject(w1, w1.getPosition());
        rules.getState().map().addGameObject(w2, w2.getPosition());

        rules.getState().gameState().setActiveWorker(w2);
        rules.getState().setChosenPosition(new Position(0,3));

        rules.movement().addExpansion(new ConsumerExpansion<>(
                (mState) -> {
                    mState.map().move(mState.activeWorkerPosition(), mState.chosenPosition());
                }, GameMarker.defaultMarker()
        ));

        rules.setPermissions(false, true);
        rules.applyMovement();
        assertEquals(new Position(0, 3), w2.getPosition(),"movementTest failed");
    }
}