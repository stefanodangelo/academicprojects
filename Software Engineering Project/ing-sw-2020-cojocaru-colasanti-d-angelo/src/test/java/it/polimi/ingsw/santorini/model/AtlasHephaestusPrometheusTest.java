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

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AtlasHephaestusPrometheusTest {
    GameDelegate delegate;
    SantoriniGame game;
    PlayersHandler playersHandler;
    Integer numberOfPlayers;
    TestingView testingView;

    @BeforeAll
    void init(){
        numberOfPlayers = 3;
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
        Integer selectedCardId1 = 6, selectedCardId2 = 10, selectedCardId3 = 4;
        ArrayList<Player> players;
        Worker w1p1, w2p1, w1p2, w2p2, w1p3, w2p3;

        testingView.setChosenCardsIds(Arrays.asList(selectedCardId3, selectedCardId1, selectedCardId2));
        testingView.setChosenPositions(Arrays.asList(new Position(0, 0), new Position(0, 3), new Position(1, 1), new Position(2, 3), new Position(3, 1), new Position(3, 4)));
        game.setupGame();
        players = PlayersHandler.getPlayers();
        w1p1 = players.get(1).getWorkers().get(0);
        w2p1 = players.get(1).getWorkers().get(1);
        w1p2= players.get(2).getWorkers().get(0);
        w2p2 = players.get(2).getWorkers().get(1);
        w1p3 = players.get(0).getWorkers().get(0);
        w2p3 = players.get(0).getWorkers().get(1);

        //Player with Hephaestus performs a normal turn and then builds two times on the same cell according to his god's power
        testingView.setChosenWorker(w1p3);
        testingView.setSkipOperations(Collections.singletonList(false));
        testingView.setChosenBlockTypes(Arrays.asList(BlockType.LEVEL1, BlockType.LEVEL2));
        testingView.setChosenPositions(Arrays.asList(new Position(1,0), new Position(0,0), new Position(0,0)));
        game.handleTurn(PlayersHandler.getCurrentPlayer(), null);

        assertAll(
                () -> assertEquals(new Position(1,0), w1p3.getPosition(), "should return the correct position"),
                () -> assertEquals(2, GameMap.getBoard()[0][0].getLevel(), "should return the correct level")
        );

        //Player with Prometheus uses his god's power and builds both before and after moving
        testingView.setChosenWorker(w1p1);
        testingView.setSkipOperations(Collections.singletonList(false));
        testingView.setChosenBlockTypes(Arrays.asList(BlockType.LEVEL1, BlockType.LEVEL2));
        testingView.setChosenPositions(Arrays.asList(new Position(2,2), new Position(1,2), new Position(2,2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(1,2), w1p1.getPosition(), "should return the correct position"),
                () -> assertEquals(2, GameMap.getBoard()[2][2].getLevel(), "should return the correct level")
        );

        //Player with Atlas uses his god's power and builds a dome on a level 0 cell
        testingView.setChosenWorker(w1p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.DOME));
        testingView.setChosenPositions(Arrays.asList(new Position(2,1), new Position(2,0)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(2,1), w1p2.getPosition(), "should return the correct position"),
                () -> assertTrue(GameMap.getBoard()[2][0].isBlocked(), "should return the correct level")
        );

        //Player with Hephaestus performs a normal turn
        testingView.setChosenWorker(w2p3);
        testingView.setSkipOperations(Collections.singletonList(true));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(0,2), new Position(0,1)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(0,2), w2p3.getPosition(), "should return the correct position")
        );

        //Player with Prometheus uses his god's power and builds both before and after moving
        testingView.setChosenWorker(w2p1);
        testingView.setSkipOperations(Collections.singletonList(false));
        testingView.setChosenBlockTypes(Arrays.asList(BlockType.LEVEL3, BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(2,2), new Position(3,2), new Position(2,3)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(3,2), w2p1.getPosition(), "should return the correct position")
        );

        //Player 1 Atlas performs a normal turn
        testingView.setChosenWorker(w2p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(3,3), new Position(3,4)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(3,3), w2p2.getPosition(), "should return the correct position")
        );

        //Player with Hephaestus performs a normal turn and then builds two times on the same cell according to his god's power
        testingView.setChosenWorker(w1p3);
        testingView.setSkipOperations(Collections.singletonList(false));
        testingView.setChosenBlockTypes(Arrays.asList(BlockType.LEVEL1, BlockType.LEVEL2));
        testingView.setChosenPositions(Arrays.asList(new Position(0,1), new Position(1,0), new Position(1,0)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(0,1), w1p3.getPosition(), "should return the correct position")
        );

        //Player with Prometheus performs a normal turn
        testingView.setChosenWorker(w1p1);
        testingView.setSkipOperations(Collections.singletonList(true));
        testingView.setChosenBlockTypes(Collections. singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(2,3), new Position(1,3)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(2,3), w1p1.getPosition(), "should return the correct position")
        );

        //Player with Atlas uses his god's power and builds a dome on a level 0 cell
        testingView.setChosenWorker(w1p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.DOME));
        testingView.setChosenPositions(Arrays.asList(new Position(1,1), new Position(2,2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(1,1), w1p2.getPosition(), "should return the correct position")
        );

        //Player with Hephaestus performs a normal turn
        testingView.setChosenWorker(w1p3);
        testingView.setSkipOperations(Collections.singletonList(true));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL3));
        testingView.setChosenPositions(Arrays.asList(new Position(0,0), new Position(1,0)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(0,0), w1p3.getPosition(), "should return the correct position")
        );

        //Player with Prometheus performs a normal turn
        testingView.setChosenWorker(w2p1);
        testingView.setSkipOperations(Collections.singletonList(true));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(3,1), new Position(3,2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(3,1), w2p1.getPosition(), "should return the correct position")
        );

        //Player with Atlas performs a normal turn
        testingView.setChosenWorker(w2p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL2));
        testingView.setChosenPositions(Arrays.asList(new Position(4,3), new Position(3,4)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(4,3), w2p2.getPosition(), "should return the correct position")
        );

        //Player with Hephaestus performs a normal turn and wins
        testingView.setChosenWorker(w1p3);
        testingView.setSkipOperations(Collections.singletonList(true));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(1,0), new Position(2,1)));
        game.handleTurn(playersHandler.next(), win -> assertTrue(win, "should have won the game"));
    }
}
