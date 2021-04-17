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
class AtlasAresTest {
    GameDelegate delegate;
    SantoriniGame game;
    Integer numberOfPlayers;
    PlayersHandler playersHandler;
    TestingView testingView;

    @BeforeAll
    void init(){
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
        Integer selectedCardId1 = 4, selectedCardId2 = 12;
        ArrayList<Player> players;
        Worker w1p1, w2p2;

        testingView.setChosenCardsIds(Arrays.asList(selectedCardId1, selectedCardId2));
        testingView.setChosenPositions(Arrays.asList(new Position(0, 0), new Position(0, 3), new Position(1, 1), new Position(2, 3)));
        game.setupGame();
        players = PlayersHandler.getPlayers();
        w1p1 = players.get(0).getWorkers().get(0);
        w2p2 = players.get(1).getWorkers().get(1);

        //Player with Atlas blocks a cell
        testingView.setChosenWorker(w1p1);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.DOME));
        testingView.setChosenPositions(Arrays.asList(new Position(1,0), new Position(0,0)));
        game.handleTurn(PlayersHandler.getCurrentPlayer(), null);

        assertAll(
                () -> assertEquals(new Position(1,0), w1p1.getPosition(), "should return the correct position"),
                () -> assertTrue(GameMap.getBoard()[0][0].isBlocked(), "cell should be blocked")
        );

        //Player with Ares performs a normal turn then tries to deconstruct Atlas's previous placed dome, but instead deconstructs a block
        testingView.setChosenWorker(w2p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setSkipOperations(Collections.singletonList(false));
        testingView.setChosenPositions(Arrays.asList(new Position(1,2), new Position(0,2), new Position(0 ,0), new Position(0, 2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(1,2), w2p2.getPosition(), "should return the correct position"),
                () -> assertTrue(GameMap.getBoard()[0][0].isBlocked(), "cell should be occupied"),
                () -> assertEquals(0, GameMap.getBoard()[0][2].getLevel(), "block should be deconstructed")
        );

        //Player with Atlas blocks a cell
        testingView.setChosenWorker(w1p1);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.DOME));
        testingView.setChosenPositions(Arrays.asList(new Position(2,1), new Position(2,2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(2,1), w1p1.getPosition(), "should return the correct position"),
                () -> assertTrue(GameMap.getBoard()[2][2].isBlocked(), "cell should be occupied")
        );

        //Player with Ares performs a normal turn then tries to deconstruct Atlas's previous placed dome, but instead deconstructs a block
        testingView.setChosenWorker(w2p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setSkipOperations(Collections.singletonList(false));
        testingView.setChosenPositions(Arrays.asList(new Position(1,3), new Position(1,2), new Position(2 ,2), new Position(1, 2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(1,3), w2p2.getPosition(), "should return the correct position"),
                () -> assertEquals(0, GameMap.getBoard()[1][2].getLevel(), "should return the correct level"),
                () -> assertTrue(GameMap.getBoard()[2][2].isBlocked(), "cell should be blocked")
        );
    }
}