package it.polimi.ingsw.santorini.model.gameoperations;

import it.polimi.ingsw.santorini.model.Player;
import it.polimi.ingsw.santorini.model.Position;
import it.polimi.ingsw.santorini.model.Worker;
import it.polimi.ingsw.santorini.model.gameoperations.result.GameOperationResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    private static void accept(GameOperationResult result2) {
        assertFalse(result2.getWinReport(), "executeTest failed");
    }

    @Test
    void executeTest() {
        Player p1 = new Player(0, null);

        Worker p1w1 = new Worker(null, null);
        p1w1.setPosition(new Position(2,0));

        Worker p1w2 = new Worker(null, null);
        p1w2.setPosition(new Position(4,0));

        ArrayList<Worker> workers = new ArrayList<>();
        workers.add(p1w1);
        workers.add(p1w2);
        p1.setWorkers(workers);

        GameOperationsController controller = new GameOperationsController(new ArrayList<>(Collections.singletonList(p1)));

        controller.getGameState().getMap().buildNextLevel(new Position(2,0));
        controller.getGameState().getMap().buildNextLevel(new Position(2,0));

        controller.getGameState().getMap().buildNextLevel(new Position(3,0));
        controller.getGameState().getMap().buildNextLevel(new Position(3,0));
        controller.getGameState().getMap().buildNextLevel(new Position(3,0));

        controller.getGameState().getMap().buildNextLevel(new Position(3,1));
        controller.getGameState().getMap().buildNextLevel(new Position(3,1));

        controller.getGameState().getMap().buildNextLevel(new Position(4,1));
        controller.getGameState().getMap().buildNextLevel(new Position(4,1));

        controller.setSkips(Arrays.asList(false, false));
        controller.setWorkers(Collections.singletonList(0));
        controller.setPositions(Collections.singletonList(new Position(3, 0)));

        Move move1 = new Move(true);
        move1.requiresWorkerSelection();
        move1.execute(controller.getGameState(), (result1) -> {
            assertTrue(result1.getWinReport(),"executeTest failed");
            assertEquals(new Position(3, 0), p1w1.getPosition(),"executeTest failed");

            controller.getGameState().getMap().buildNextLevel(new Position(2,0));
            controller.getGameState().getMap().buildNextLevel(new Position(2,0));

            controller.getGameState().getMap().buildNextLevel(new Position(2,1));
            controller.getGameState().getMap().buildNextLevel(new Position(2,1));
            controller.getGameState().getMap().buildNextLevel(new Position(2,1));
            controller.getGameState().getMap().buildNextLevel(new Position(2,1));

            controller.getGameState().getMap().buildNextLevel(new Position(3,1));
            controller.getGameState().getMap().buildNextLevel(new Position(3,1));

            controller.getGameState().getMap().buildNextLevel(new Position(4,1));
            controller.getGameState().getMap().buildNextLevel(new Position(4,1));

            Move move2 = new Move(true);
            move2.requiresWorkerSelection();
            move2.execute(controller.getGameState(), MoveTest::accept);
        });
    }
}