package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.controller.CardLoader;
import it.polimi.ingsw.santorini.testing.controller.TestingController;
import it.polimi.ingsw.santorini.testing.view.TestingView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArtemisAthenaTest {
    GameDelegate delegate;
    SantoriniGame game;
    PlayersHandler playersHandler;
    Integer numberOfPlayers;
    TestingView testingView;

    @BeforeAll
    void init() {
        numberOfPlayers = 2;
        delegate = new TestingController();
        game = new SantoriniGame(delegate, numberOfPlayers);
        SantoriniGame.createMap();
        CardLoader.loadCards();
        playersHandler = game.getPlayersHandler();
        testingView = TestingController.getTestingView();
        testingView.setGameMode(1);
        testingView.setNumOfPlayers(numberOfPlayers);
    }

    @Test
    void test() {
        Integer selectedCardId1 = 2, selectedCardId2 = 3;
        ArrayList<Player> players;
        Worker w1p1, w1p2, w2p1, w2p2;

        testingView.setChosenCardsIds(Arrays.asList(selectedCardId2, selectedCardId1));
        testingView.setChosenPositions(Arrays.asList(new Position(0, 0), new Position(0, 3), new Position(1, 1), new Position(2, 3)));
        game.setupGame();
        players = PlayersHandler.getPlayers();
        w1p1 = players.get(1).getWorkers().get(0);
        w2p1 = players.get(1).getWorkers().get(1);
        w1p2 = players.get(0).getWorkers().get(0);
        w2p2 = players.get(0).getWorkers().get(1);
        playersHandler.next();

        //Player with Artemis uses god's power and moves his worker twice
        testingView.setChosenWorker(w1p1);
        testingView.setSkipOperations(Collections.singletonList(false));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(2, 1), new Position(3, 1), new Position(3, 2)));
        game.handleTurn(PlayersHandler.getCurrentPlayer(), null);

        assertAll(
                () -> assertEquals(new Position(3, 1), w1p1.getPosition(),"should return the correct position")
        );

        //Player with Athena moves one worker up one level, thus activating Athena's power. The enemy player can't move up on the next turn
        testingView.setChosenWorker(w1p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(0, 1), new Position(0, 2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(0, 1), w1p2.getPosition(),"should return the correct position")
        );

        //Player with Artemis performs a normal turn
        testingView.setChosenWorker(w2p1);
        testingView.setSkipOperations(Collections.singletonList(true));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(3, 2), new Position(2, 2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(3, 2), w2p1.getPosition(),"should return the correct position")
        );

        //Player with Athena moves one worker up one level, thus activating Athena's power. The enemy player can't move up on the next turn
        testingView.setChosenWorker(w2p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(0, 2), new Position(1, 2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(0, 2), w2p2.getPosition(),"should return the correct position")
        );

        //Player with Artemis tries to move up, but can't, then performs a normal turn
        testingView.setChosenWorker(w1p1);
        testingView.setSkipOperations(Collections.singletonList(true));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL2));
        testingView.setChosenPositions(Arrays.asList(new Position(2, 2), new Position(2, 1), new Position(2, 2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(2, 1), w1p1.getPosition(),"should return the correct position")
        );

        //Player with Athena moves one worker up one level, thus activating Athena's power. The enemy player can't move up on the next turn
        testingView.setChosenWorker(w1p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL3));
        testingView.setChosenPositions(Arrays.asList(new Position(1, 2), new Position(2, 2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(1, 2), w1p2.getPosition(),"should return the correct position")
        );

        //Player with Artemis uses its power, thus moving twice
        testingView.setChosenWorker(w2p1);
        testingView.setSkipOperations(Collections.singletonList(false));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(3, 1), new Position(2, 0), new Position(1, 0)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(2, 0), w2p1.getPosition(),"should return the correct position")
        );
    }
}

