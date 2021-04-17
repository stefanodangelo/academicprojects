package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.controller.CardLoader;
import it.polimi.ingsw.santorini.testing.controller.TestingController;
import it.polimi.ingsw.santorini.testing.view.TestingView;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HeraPanTest {
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
    void test() {
        Integer selectedCardId1 = 20, selectedCardId2 = 9;
        ArrayList<Player> players;
        Worker w1p1, w1p2, w2p2;

        testingView.setChosenCardsIds(Arrays.asList(selectedCardId2, selectedCardId1));
        testingView.setChosenPositions(Arrays.asList( new Position(0, 0), new Position(0, 3), new Position(1, 1), new Position(2, 3)));
        game.setupGame();
        players = PlayersHandler.getPlayers();
        w1p1 = players.get(0).getWorkers().get(0);
        w1p2 = players.get(1).getWorkers().get(0);
        w2p2 = players.get(1).getWorkers().get(1);

        //Player with Pan performs a normal turn
        testingView.setChosenWorker(w1p1);
        testingView.setChosenPositions(Arrays.asList(new Position(0, 1), new Position(0, 2)));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        game.handleTurn(PlayersHandler.getCurrentPlayer(), null);

        //Player with Hera performs a normal turn
        testingView.setChosenWorker(w2p2);
        testingView.setChosenPositions(Arrays.asList(new Position(1, 3), new Position(1, 2)));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        game.handleTurn(playersHandler.next(), null);

        //Player with Pan moves up
        testingView.setChosenWorker(w1p1);
        testingView.setChosenPositions(Arrays.asList(new Position(0, 2), new Position(1, 2)));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL2));
        game.handleTurn(playersHandler.next(), null);

        //Player with Hera performs a normal turn
        testingView.setChosenWorker(w1p2);
        testingView.setChosenPositions(Arrays.asList(new Position(2, 2), new Position(2, 1)));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        game.handleTurn(playersHandler.next(), null);

        //Pan shouldn't win because climbed down two levels into a perimeter Cell
        testingView.setChosenWorker(w1p1);
        testingView.setChosenPositions(Arrays.asList(new Position(0, 1), new Position(0, 0)));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        game.handleTurn(playersHandler.next(), null);

        assertEquals(new Position(0, 1), w1p1.getPosition(), "Worker should still be on the map");
    }
}
