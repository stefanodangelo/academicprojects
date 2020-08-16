package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.controller.CardLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CardLoaderTest {
    CardLoader cardLoader = CardLoader.getInstance();

    @Test
    void allMethodsTest() {
        CardLoader.loadCards();
        ArrayList<Card> cards = CardLoader.getCards();
        assertAll(
                    () -> assertSame(cardLoader, CardLoader.getInstance(), "should return the same instance of the object"),
                    () -> assertEquals(14, cards.size(), "should return the correct size")
                );
        for(Card card : cards)
            assertAll(
                        () -> assertNotNull(card.getId()),
                        () -> assertNotNull(card.getName()),
                        () -> assertNotNull(card.getDescription()),
                        () -> assertNotNull(card.getTitle()),
                        () -> assertNotNull(card.getTrigger()),
                        () -> assertNotNull(card.getPower()),
                        () -> assertNotNull(card.getImage())
                     );
    }

}