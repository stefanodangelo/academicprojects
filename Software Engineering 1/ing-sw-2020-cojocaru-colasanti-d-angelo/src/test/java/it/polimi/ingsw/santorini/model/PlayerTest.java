package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.controller.CardLoader;
import it.polimi.ingsw.santorini.view.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    static Player player;
    static Card card;
    static ArrayList<Worker> workers = new ArrayList<>(2);
    static CardLoader cardLoader;
    static int index = (int) (Math.random() * 100) % 14;

    @BeforeAll
    static void init(){
        player = new Player(1, "Pippo");
        cardLoader = CardLoader.getInstance();
        CardLoader.loadCards();
        card = CardLoader.getCards().get(index);
        workers.add(new Worker(Color.YELLOW.getName(), Gender.MALE));
        workers.add(new Worker(Color.YELLOW.getName(), Gender.FEMALE));
    }

    @Test
    void getIdTest() {
        assertAll(
                  () -> assertEquals(1, player.getId(), "should return the correct player's id"),
                  () -> assertNotNull(player.getId(),"should not return a null id")
                  );
    }

    @Test
    void getNameTest() {
        assertAll(
                  () -> assertEquals("Pippo", player.getName(), "should return the correct player's name"),
                  () -> assertNotNull(player.getName(), "should not return a null player's name")
                  );
    }

    @Test
    void getCardTest() {
        player.setCard(card);
        assertEquals(player.getCard(), card, "should return the correct com.softeng17.santorini.model.Card");
    }

    @Test
    void getWorkersTest() {
        player.setWorkers(workers);
        assertEquals(workers, player.getWorkers(), "should return the correct same list of workers");
    }

    @Test
    void setCardTest() {
        player.setCard(card);
        assertEquals(card, player.getCard(), "should set the correct card");
    }

    @Test
    void setWorkersTest() {
        player.setWorkers(workers);
        assertEquals(workers, player.getWorkers(), "should set the correct worker array");
    }
}