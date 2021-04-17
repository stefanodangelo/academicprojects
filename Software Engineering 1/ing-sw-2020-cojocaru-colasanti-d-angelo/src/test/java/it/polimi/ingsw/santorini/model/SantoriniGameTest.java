package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.controller.CardLoader;
import it.polimi.ingsw.santorini.testing.controller.TestingController;
import it.polimi.ingsw.santorini.testing.view.TestingView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SantoriniGameTest {
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
        playersHandler = game.getPlayersHandler();
        testingView = TestingController.getTestingView();
        testingView.setNumOfPlayers(3);
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class SetupGameTest{
        CardLoader cardLoader;
        List<Integer> cardIds;

        @BeforeAll
        void init(){
            cardIds = new ArrayList<>();
            ArrayList<Card> cards;

            cardLoader = CardLoader.getInstance();
            CardLoader.loadCards();
            SantoriniGame.createMap();
            cards = CardLoader.getCards();
            for(Card card : cards)
                cardIds.add(card.getId());
        }

        @Test
        void setupGameTest(){
            Player p1 = new Player(1, "Ciccio"), p2 = new Player(2, "Turi"), p3 = new Player(3, "Iaffiu");
            Integer selectedCardId1 = 12, selectedCardId2 = 4, selectedCardId3 = 27;
            ArrayList<Player> players;

            testingView.setGameMode(2);
            testingView.setChosenCardsIds(Arrays.asList(selectedCardId2, selectedCardId3, selectedCardId1));
            testingView.setChosenPositions(Arrays.asList(new Position(0, 0), new Position(0, 3), new Position(1, 1), new Position(2, 3), new Position(3, 1), new Position(3, 4)));
            game.setupGame();
            players = PlayersHandler.getPlayers();
            assertAll(  //players acceptance test
                        () -> assertAll(
                                        () -> assertEquals(numberOfPlayers, players.size(), "should return the correct size"),
                                        () -> assertEquals(p1.getName(), players.get(0).getName(), "should return the correct id"),
                                        () -> assertEquals(p2.getName(), players.get(1).getName(), "should return the correct id"),
                                        () -> assertEquals(p3.getName(), players.get(2).getName(), "should return the correct id")
                                       ),

                        //game mod choice test
                        () -> assertEquals(GameMode.ADVANCED, SantoriniGame.getMode(), "should return the correct chosen mode"),

                        //most god-like player choice test
                        () -> assertEquals(p1.getId(), new Poll(numberOfPlayers, null).getLeader(players).getId(), "should return the correct most voted player"),

                        //cards loading, selection and assignment test
                        () -> assertAll(
                                        () -> assertEquals(selectedCardId1, players.get(1).getCard().getId(), "should return the correct assigned card's id"),
                                        () -> assertEquals(selectedCardId2, players.get(2).getCard().getId(), "should return the correct assigned card's id"),
                                        () -> assertEquals(selectedCardId3, players.get(0).getCard().getId(), "should return the correct assigned card's id")
                                       ),

                        //first player choice test
                        () -> assertEquals(p1.getId(), PlayersHandler.getCurrentPlayer().getId(), "should return the correct current player")
                     );
            //workers choices and positioning test
            for(int i = 0; i < numberOfPlayers; i++){
                assertNotNull(players.get(i).getWorkers());
                assertEquals(2, players.get(i).getWorkers().size(), "should return the correct size");
                if(i < 2)
                    assertNotEquals(players.get(i).getWorkers().get(0).getColor(), players.get(i+1).getWorkers().get(0).getColor());
                assertNotNull(players.get(i).getWorkers().get(0).getPosition());
                assertNotNull(players.get(i).getWorkers().get(1).getPosition());
            }
        }
    }
}