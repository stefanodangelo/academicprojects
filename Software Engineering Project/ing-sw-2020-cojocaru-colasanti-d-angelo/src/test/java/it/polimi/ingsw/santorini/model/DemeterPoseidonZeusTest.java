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
public class DemeterPoseidonZeusTest {
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
        Integer selectedCardId1 = 5, selectedCardId2 = 27, selectedCardId3 = 30;
        ArrayList<Player> players;
        Worker w1p1, w2p1, w1p2, w2p2, w1p3, w2p3;

        testingView.setChosenCardsIds(Arrays.asList(selectedCardId3, selectedCardId1, selectedCardId2));
        testingView.setChosenPositions(Arrays.asList(new Position(0, 0), new Position(0, 3), new Position(1, 1), new Position(2, 3), new Position(3, 1), new Position(3, 4)));
        game.setupGame();
        players = PlayersHandler.getPlayers();
        w1p2 = players.get(1).getWorkers().get(0);
        w2p2 = players.get(1).getWorkers().get(1);
        w1p3= players.get(2).getWorkers().get(0);
        w2p3 = players.get(2).getWorkers().get(1);
        w1p1 = players.get(0).getWorkers().get(0);
        w2p1 = players.get(0).getWorkers().get(1);

        //Player with Demeter uses his god's power and builds twice
        testingView.setChosenWorker(w1p1);
        testingView.setSkipOperations(Collections.singletonList(false));
        testingView.setChosenBlockTypes(Arrays.asList(BlockType.LEVEL1, BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(0,1), new Position(0,0), new Position(1,0)));
        game.handleTurn(PlayersHandler.getCurrentPlayer(), null);

        assertEquals(1, GameMap.getBoard()[1][0].getLevel(), "should have built twice successfully");

        //Player with Poseidon uses his god's power and builds three times
        testingView.setChosenWorker(w1p2);
        testingView.setSkipOperations(Arrays.asList(false, false, false));
        testingView.setChosenBlockTypes(Arrays.asList(BlockType.LEVEL1, BlockType.LEVEL1, BlockType.LEVEL2, BlockType.LEVEL3));
        testingView.setChosenPositions(Arrays.asList(new Position(1,2), new Position(2,2), new Position(2,4), new Position(2,4), new Position(2,4)));
        game.handleTurn(playersHandler.next(), null);

        //Player with Zeus's power activates and the worker builds underneath itself
        testingView.setChosenWorker(w1p3);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(3,2), new Position(3,2)));
        game.handleTurn(playersHandler.next(), null);

        assertEquals(1, GameMap.getBoard()[3][2].getLevel(), "should have built underneath itself successfully");

        //Player with Demeter uses his god's power and builds twice
        testingView.setChosenWorker(w2p1);
        testingView.setSkipOperations(Collections.singletonList(false));
        testingView.setChosenBlockTypes(Arrays.asList(BlockType.LEVEL1, BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(1,3), new Position(0,2), new Position(0,3)));
        game.handleTurn(playersHandler.next(), null);

        //Player with Poseidon uses his god's power and builds three times
        testingView.setChosenWorker(w2p2);
        testingView.setSkipOperations(Arrays.asList(false, false, false));
        testingView.setChosenBlockTypes(Arrays.asList(BlockType.LEVEL2, BlockType.LEVEL3, BlockType.DOME, BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(2,2), new Position(1,1), new Position(0,2), new Position(0,2), new Position(0,2)));
        game.handleTurn(playersHandler.next(), null);

        //Player with Zeus's power activates and the worker builds underneath itself
        testingView.setChosenWorker(w2p3);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(3,3), new Position(3,3)));
        game.handleTurn(playersHandler.next(), null);

        //Player with Demeter performs its normal turn
        testingView.setChosenWorker(w1p1);
        testingView.setSkipOperations(Collections.singletonList(true));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(0,0), new Position(0,1)));
        game.handleTurn(playersHandler.next(), null);

        //Player with Poseidon can't use his god's power because his unmoved worker isn't at a ground level
        testingView.setChosenWorker(w1p2);
        testingView.setSkipOperations(Collections.singletonList(false));
        testingView.setChosenBlockTypes(Arrays.asList(BlockType.LEVEL1, BlockType.LEVEL2));
        testingView.setChosenPositions(Arrays.asList(new Position(0,3), new Position(1,2), new Position(1, 2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(0, 3), w1p2.getPosition(), "should return the correct position"),
                () -> assertEquals(1, GameMap.getBoard()[1][2].getLevel(), "should not have upgraded its level")
        );

        //Player with Zeus performs a normal turn
        testingView.setChosenWorker(w1p3);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(3,1), new Position(3,2)));
        game.handleTurn(playersHandler.next(), null);

        //Player with Demeter uses his god's power and builds twice
        testingView.setChosenWorker(w2p1);
        testingView.setSkipOperations(Collections.singletonList(false));
        testingView.setChosenBlockTypes(Arrays.asList(BlockType.LEVEL1, BlockType.LEVEL3));
        testingView.setChosenPositions(Arrays.asList(new Position(2,3), new Position(1,3), new Position(3,2)));
        game.handleTurn(playersHandler.next(), null);

        //Player with Poseidon can't use his god's power because his unmoved worker isn't at a ground level
        testingView.setChosenWorker(w2p2);
        testingView.setSkipOperations(Collections.singletonList(false));
        testingView.setChosenBlockTypes(Arrays.asList(BlockType.LEVEL1, BlockType.LEVEL2));
        testingView.setChosenPositions(Arrays.asList(new Position(2,1), new Position(2,0), new Position(1,3)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(2, 1), w2p2.getPosition(), "should return the correct position"),
                () -> assertEquals(1, GameMap.getBoard()[1][3].getLevel(), "should not have upgraded its level")
        );

        //Player with Zeus activates its power and the worker builds underneath itself
        testingView.setChosenWorker(w2p3);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL2));
        testingView.setChosenPositions(Arrays.asList(new Position(2,2), new Position(2,2)));
        game.handleTurn(playersHandler.next(), null);

        //Player with Demeter performs a normal turn
        testingView.setChosenWorker(w2p1);
        testingView.setSkipOperations(Collections.singletonList(true));
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(1,4), new Position(2,3)));
        game.handleTurn(playersHandler.next(), null);

        //Player with Poseidon uses his god's power and builds twice
        testingView.setChosenWorker(w1p2);
        testingView.setSkipOperations(Arrays.asList(false, false, true));
        testingView.setChosenBlockTypes(Arrays.asList(BlockType.LEVEL2, BlockType.LEVEL1, BlockType.LEVEL2, BlockType.DOME));
        testingView.setChosenPositions(Arrays.asList(new Position(1,2), new Position(2,3), new Position(3,0), new Position(3,0), new Position(3,2)));
        game.handleTurn(playersHandler.next(), null);

        //Player with Zeus performs a normal turn and wins
        testingView.setChosenWorker(w2p3);
        testingView.setChosenPositions(Collections.singletonList(new Position(3, 2)));
        game.handleTurn(playersHandler.next(), win -> assertTrue(win, "should have won the game"));
    }
}
