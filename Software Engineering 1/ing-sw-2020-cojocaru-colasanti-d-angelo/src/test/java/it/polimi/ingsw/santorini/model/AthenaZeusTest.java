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
public class AthenaZeusTest {
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
        testingView.setGameMode(2);
        testingView.setNumOfPlayers(numberOfPlayers);
    }

    @Test
    void test(){
        Integer selectedCardId1 = 3, selectedCardId2 = 30;
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
        Player c = PlayersHandler.getCurrentPlayer();

        //Player with Zeus uses his god's power and builds underneath itself
        testingView.setChosenWorker(w1p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(0, 1), new Position(0, 1)));
        game.handleTurn(PlayersHandler.getCurrentPlayer(), null);

        assertAll(
                () -> assertEquals(new Position(0, 1), w1p2.getPosition(), "should return the correct position")
                );

        //Player with Athena performs a normal turn
        testingView.setChosenWorker(w2p1);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(1, 3), new Position(1, 2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(1, 3), w2p1.getPosition(), "should return the correct position")
                );

        //Player with Zeus performs a normal turn
        testingView.setChosenWorker(w2p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(0, 2), new Position(0, 3)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(0, 2), w2p2.getPosition(), "should return the correct position")
                );

        //Player with Athena moves up, thus activating Athena's power. The enemy player's worker can't move up next turn
        testingView.setChosenWorker(w1p1);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(1, 2), new Position(2, 2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(1, 2), w1p1.getPosition(), "should return the correct position")
        );

        //Player with Zeus uses his god's power and builds underneath itself, even though Athena's power is active
        testingView.setChosenWorker(w1p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(1, 1), new Position(1, 1)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(1, 1), w1p2.getPosition(), "should return the correct position")
        );

        //Player with Athena moves up, thus activating Athena's power. The enemy player's worker can't move up next turn
        testingView.setChosenWorker(w2p1);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(2, 2), new Position(2, 1)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(2, 2), w2p1.getPosition(), "should return the correct position")
        );

        //Player with Zeus tries moving up, but can't, then performs a normal turn
        testingView.setChosenWorker(w2p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL2));
        testingView.setChosenPositions(Arrays.asList(new Position(0, 1), new Position(0, 3), new Position(1, 3), new Position(0, 3)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(1, 3), w2p2.getPosition(), "should return the correct position")
        );
    }
}
