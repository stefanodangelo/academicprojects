package it.polimi.ingsw.santorini.model;

import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TwoPlayersPollTest{
    Poll poll;
    PlayersHandler playersHandler;
    Player p1, p2;

    @BeforeAll
    void init(){
        poll = new Poll(2, null);
        playersHandler = new PlayersHandler();
        p1 = new Player(518034, "Ciccio");
        p2 = new Player(1122, "Turi");
        playersHandler.addPlayer(p1);
        playersHandler.addPlayer(p2);
    }

    @Nested
    class VoteTest{
        @AfterEach
        void reinitialize(){
            Arrays.fill(poll.getLeaderboard(), 0);
        }

        @Test
        void whenPlayerOneWins() {
            poll.vote(p1.getId(), PlayersHandler.getPlayers());
            poll.vote(p1.getId(), PlayersHandler.getPlayers());

            assertAll( () -> assertEquals(2, poll.getLeaderboard()[0], "should return the correct total of votes"),
                       () -> assertEquals(0, poll.getLeaderboard()[1], "should return the correct total of votes")
            );
        }

        @Test
        void whenPlayerTwoWins() {
            poll.vote(p2.getId(), PlayersHandler.getPlayers());
            poll.vote(p2.getId(), PlayersHandler.getPlayers());

            assertAll( () -> assertEquals(0, poll.getLeaderboard()[0], "should return the correct total of votes"),
                       () -> assertEquals(2, poll.getLeaderboard()[1], "should return the correct total of votes")
            );
        }

        @Test
        void whenDraw() {
            poll.vote(p1.getId(), PlayersHandler.getPlayers());
            poll.vote(p2.getId(), PlayersHandler.getPlayers());

            assertEquals(poll.getLeaderboard()[0], poll.getLeaderboard()[1], "should return the correct total of votes");
        }
    }

    @Nested
    class GetLeaderTest{
        @AfterEach
        void reinitialize(){
            Arrays.fill(poll.getLeaderboard(), 0);
        }

        @Test
        void whenPlayerOneWins() {
            poll.vote(p1.getId(), PlayersHandler.getPlayers());
            poll.vote(p1.getId(), PlayersHandler.getPlayers());

            assertEquals(p1, poll.getLeader(PlayersHandler.getPlayers()), "should return the correct leader");
        }

        @Test
        void whenPlayerTwoWins() {
            poll.vote(p2.getId(), PlayersHandler.getPlayers());
            poll.vote(p2.getId(), PlayersHandler.getPlayers());

            assertEquals(p2, poll.getLeader(PlayersHandler.getPlayers()), "should return the correct leader");
        }

        @Test
        void whenDraw() {
            poll.vote(p1.getId(), PlayersHandler.getPlayers());
            poll.vote(p2.getId(), PlayersHandler.getPlayers());

            assertEquals(p1, poll.getLeader(PlayersHandler.getPlayers()), "should return the first player in the list who has got the maximum number of votes");
        }
    }
}
