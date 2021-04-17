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
class AthenaMinotaurApolloTest {
    GameDelegate delegate;
    SantoriniGame game;
    Integer numberOfPlayers;
    PlayersHandler playersHandler;
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
        testingView.setGameMode(1);
        testingView.setNumOfPlayers(numberOfPlayers);
    }

    @Test
    void test(){
        Integer selectedCardId1 = 3, selectedCardId2 = 1, selectedCardId3 = 8;
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
        Player c = playersHandler.next();

        //Player with Apollo swaps his worker's position with one of Minotaur's workers
        testingView.setChosenWorker(w2p1);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(3,4), new Position(2,4)));
        game.handleTurn(PlayersHandler.getCurrentPlayer(), null);

        assertAll(
                () -> assertEquals(new Position(3,4), w2p1.getPosition(), "should return the correct position"),
                () -> assertEquals(new Position(2,3), w2p2.getPosition(), "should return the correct position")
        );

        //Player with Minotaur performs a normal turn
        testingView.setChosenWorker(w1p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(2,1), new Position(2,2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(2,1), w1p2.getPosition(), "should return the correct position")
        );

        //Player with Athena performs a normal turn
        testingView.setChosenWorker(w2p3);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(0,2), new Position(0,1)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(0,2), w2p3.getPosition(), "should return the correct position")
        );

        //Player with Apollo swaps his worker with one of Minotaur's ones
        testingView.setChosenWorker(w1p1);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(2,1), new Position(3,2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(2,1), w1p1.getPosition(), "should return the correct position"),
                () -> assertEquals(new Position(1,1), w1p2.getPosition(), "should return the correct position")
        );

        //Player with Minotaur performs a normal turn
        testingView.setChosenWorker(w2p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL2));
        testingView.setChosenPositions(Arrays.asList(new Position(2,2), new Position(3, 2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(2,2), w2p2.getPosition(), "should return the correct position")
        );

        //Player with Athena moves up, thus activating Athena's effect. On their next turn enemies can't move up
        testingView.setChosenWorker(w1p3);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(0,1), new Position(1,2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(0,1), w1p3.getPosition(), "should return the correct position")
        );

        //Player with Apollo tries moving up, but can't. It performs a normal turn after that
        testingView.setChosenWorker(w2p1);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL1));
        testingView.setChosenPositions(Arrays.asList(new Position(2,4), new Position(3,3), new Position(2,3)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(3,3), w2p1.getPosition(), "should return the correct position")
        );

        //Player with Minotaur pushes Apollo's worker backwards
        testingView.setChosenWorker(w1p2);
        testingView.setChosenBlockTypes(Collections.singletonList(BlockType.LEVEL2));
        testingView.setChosenPositions(Arrays.asList(new Position(2,1), new Position(1,2)));
        game.handleTurn(playersHandler.next(), null);

        assertAll(
                () -> assertEquals(new Position(2,1), w1p2.getPosition(), "should return the correct position"),
                () -> assertEquals(new Position(3,1), w1p1.getPosition(), "should return the correct position")
        );
    }
}
