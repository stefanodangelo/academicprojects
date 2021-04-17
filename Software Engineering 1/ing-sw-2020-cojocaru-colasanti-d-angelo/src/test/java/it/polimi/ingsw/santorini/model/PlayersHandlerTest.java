package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.model.exceptions.NoPlayerWithSuchColorException;
import it.polimi.ingsw.santorini.model.exceptions.NoPlayerWithSuchIdException;
import it.polimi.ingsw.santorini.view.Color;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayersHandlerTest {
    static PlayersHandler playersHandler;

    @BeforeAll
    static void init(){
        playersHandler = new PlayersHandler();
    }

    @Nested
    class TwoPlayersHandler{
        Integer id1 = 1, id2 = 2;
        Player p1 = new Player(id1, "Paper1n0"), p2 = new Player(id2,"Plut0");

        @BeforeEach
        void init() {
            playersHandler.addPlayer(p1);
            playersHandler.addPlayer(p2);
        }

        @AfterEach
        void remove() {
            PlayersHandler.removePlayer(p1);
            PlayersHandler.removePlayer(p2);
        }

        @Test
        void getPlayersTest(){
            assertAll(
                        () -> assertEquals(2, PlayersHandler.getPlayers().size(), "should return the correct number of players"),
                        () -> assertEquals(p1, PlayersHandler.getPlayers().get(0), "should return the right player"),
                        () -> assertEquals(p2, PlayersHandler.getPlayers().get(1), "should return the right player")
                     );
        }

        @Test
        void getPlayerByIdTest() {
            Integer id = 3;
            assertAll(
                        () -> assertThrows(NoPlayerWithSuchIdException.class, () -> PlayersHandler.getPlayerById(id), "should throw an exception when searching a player with no such id"),
                        () -> assertEquals(p1, PlayersHandler.getPlayerById(id1), "should return the correct player"),
                        () -> assertEquals(p2, PlayersHandler.getPlayerById(id2), "should return the correct player")
                     );
        }

        @Test
        void getPlayerByColorTest() {
            Color color1 = Color.BLUE;
            Color color2 = Color.YELLOW;
            Color color3 = Color.WHITE;
            ArrayList<Worker> w1 = new ArrayList<>(2), w2 = new ArrayList<>(2);

            for(int i = 0; i < 2; i++) {
                w1.add(new Worker(color1.getName(), Gender.values()[i]));
                w2.add(new Worker(color2.getName(), Gender.values()[i]));
            }
            p1.setWorkers(w1);
            p2.setWorkers(w2);

            assertAll(
                        () -> assertThrows(NoPlayerWithSuchColorException.class, () -> playersHandler.getPlayerByColor(color3.getName()), "should throw an exception when searching a player with no such color"),
                        () -> assertEquals(p1, playersHandler.getPlayerByColor(color1.getName()), "should return the correct player"),
                        () -> assertEquals(p2, playersHandler.getPlayerByColor(color2.getName()), "should return the correct player")
                     );
        }
    }

    @Nested
    class ThreePlayersHandler {
        Integer id1 = 1, id2 = 2, id3 = 3;
        Player p1 = new Player(id1, "Paper1n0"), p2 = new Player(id2, "Plut0"), p3 = new Player(id3, "P1pp0");


        @BeforeEach
        void init() {
            playersHandler.addPlayer(p1);
            playersHandler.addPlayer(p2);
            playersHandler.addPlayer(p3);
        }

        @AfterEach
        void remove() {
            PlayersHandler.removePlayer(p1);
            PlayersHandler.removePlayer(p2);
            PlayersHandler.removePlayer(p3);
        }

        @Test
        void getPlayersTest() {
            assertAll(
                        () -> assertEquals(3, PlayersHandler.getPlayers().size(), "should return the correct number of players"),
                        () -> assertEquals(p1, PlayersHandler.getPlayers().get(0), "should return the right player"),
                        () -> assertEquals(p2, PlayersHandler.getPlayers().get(1), "should return the right player"),
                        () -> assertEquals(p3, PlayersHandler.getPlayers().get(2), "should return the right player")
                    );
        }

        @Test
        void getPlayerByIdTest() {
            Integer id = 4;
            assertAll(
                    () -> assertThrows(NoPlayerWithSuchIdException.class, () -> PlayersHandler.getPlayerById(id), "should throw an exception when searching a player with no such id"),
                    () -> assertEquals(p1, PlayersHandler.getPlayerById(id1), "should return the correct player"),
                    () -> assertEquals(p2, PlayersHandler.getPlayerById(id2), "should return the correct player"),
                    () -> assertEquals(p3, PlayersHandler.getPlayerById(id3), "should return the correct player")
            );
        }

        @Test
        void getPlayerByColorTest() {
            Color color1 = Color.BLUE;
            Color color2 = Color.YELLOW;
            Color color3 = Color.WHITE;
            ArrayList<Worker> w1 = new ArrayList<>(2), w2 = new ArrayList<>(2), w3 = new ArrayList<>(2);

            for(int i = 0; i < 2; i++) {
                w1.add(new Worker(color1.getName(), Gender.values()[i]));
                w2.add(new Worker(color2.getName(), Gender.values()[i]));
                w3.add(new Worker(color3.getName(), Gender.values()[i]));
            }
            p1.setWorkers(w1);
            p2.setWorkers(w2);
            p3.setWorkers(w3);

            assertAll(
                        () -> assertEquals(p1, playersHandler.getPlayerByColor(color1.getName()), "should return the correct player"),
                        () -> assertEquals(p2, playersHandler.getPlayerByColor(color2.getName()), "should return the correct player"),
                        () -> assertEquals(p3, playersHandler.getPlayerByColor(color3.getName()), "should return the correct player")
                     );
        }
    }
}