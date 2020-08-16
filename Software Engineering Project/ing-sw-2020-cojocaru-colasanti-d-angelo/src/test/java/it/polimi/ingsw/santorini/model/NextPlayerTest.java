package it.polimi.ingsw.santorini.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NextPlayerTest {
    static PlayersHandler playersHandler;

    @BeforeAll
    static void init(){
        playersHandler = new PlayersHandler();
        playersHandler.addPlayer(new Player(0, "player1"));
        playersHandler.addPlayer(new Player(1, "player2"));
        playersHandler.addPlayer(new Player(2, "player3"));
    }

    @RepeatedTest(10)
    void nextTest(RepetitionInfo repetitionInfo){
        int index = repetitionInfo.getCurrentRepetition() % 3;  //rep.1 => 1 % 3 = 1
                                                                //rep.2 => 2 % 3 = 2
                                                                //rep.3 => 3 % 3 = 0, and so on
        assertAll(() -> assertEquals(index, playersHandler.next().getId(), "should return the correct id"));
    }
}
