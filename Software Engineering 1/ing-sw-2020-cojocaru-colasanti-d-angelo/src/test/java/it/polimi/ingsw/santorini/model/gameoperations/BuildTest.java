package it.polimi.ingsw.santorini.model.gameoperations;

import it.polimi.ingsw.santorini.model.BlockType;
import it.polimi.ingsw.santorini.model.Player;
import it.polimi.ingsw.santorini.model.Position;
import it.polimi.ingsw.santorini.model.Worker;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BuildTest {

    @Test
    void executeTest() {
        Player p1 = new Player(0, null);

        Worker p1w1 = new Worker(null, null);
        p1w1.setPosition(new Position(3,0));

        Worker p1w2 = new Worker(null, null);
        p1w2.setPosition(new Position(4,0));

        ArrayList<Worker> workers = new ArrayList<>();
        workers.add(p1w1);
        workers.add(p1w2);
        p1.setWorkers(workers);

        GameOperationsController controller = new GameOperationsController(new ArrayList<>(Collections.singletonList(p1)));

        controller.getGameState().getMap().buildNextLevel(new Position(2,0));
        controller.getGameState().getMap().buildNextLevel(new Position(2,0));
        controller.getGameState().getMap().buildNextLevel(new Position(2,0));

        controller.getGameState().getMap().buildNextLevel(new Position(2,1));
        controller.getGameState().getMap().buildNextLevel(new Position(2,1));
        controller.getGameState().getMap().buildNextLevel(new Position(2,1));
        controller.getGameState().getMap().buildNextLevel(new Position(2,1));

        controller.getGameState().getMap().buildNextLevel(new Position(3,1));
        controller.getGameState().getMap().buildNextLevel(new Position(3,1));
        controller.getGameState().getMap().buildNextLevel(new Position(3,1));
        controller.getGameState().getMap().buildNextLevel(new Position(3,1));

        controller.getGameState().getMap().buildNextLevel(new Position(4,1));
        controller.getGameState().getMap().buildNextLevel(new Position(4,1));
        controller.getGameState().getMap().buildNextLevel(new Position(4,1));
        controller.getGameState().getMap().buildNextLevel(new Position(4,1));

        controller.setSkips(Arrays.asList(false,false));
        controller.setWorkers(Collections.singletonList(0));
        controller.setPositions(Collections.singletonList(new Position(2, 0)));
        controller.setBlockTypes(Collections.singletonList(BlockType.DOME));

        Build build1 = new Build(true);
        build1.requiresWorkerSelection();
        build1.execute(controller.getGameState(), (result1) -> {
            assertNull(result1.getWinReport(),"executeTest failed");
            assertEquals(4, controller.getGameState().getMap().cellAt(new Position(2,0)).getLevel()
            ,"executeTest failed");

            Build build2 = new Build(true);
            build2.execute(controller.getGameState(), (result2) -> assertFalse(result2.getWinReport(), "executeTest failed"));
        });
    }
}