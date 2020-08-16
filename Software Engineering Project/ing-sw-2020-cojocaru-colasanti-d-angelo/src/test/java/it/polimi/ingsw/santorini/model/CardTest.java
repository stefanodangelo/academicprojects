package it.polimi.ingsw.santorini.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CardTest {
    static Card card;
    static String name, id, title, description,trigger, advanced;
    static HashMap<String, String> cardContent;

    @BeforeAll
    static void init() {
        cardContent = new HashMap<>();
        name = "testName";
        id = "0";
        title = "testTitle";
        description = "testDescription";
        trigger = "YOURMOVE";
        advanced = "false";
        cardContent.put("name", "testName");
        cardContent.put("id", "0");
        cardContent.put("title", "testTitle");
        cardContent.put("description", "testDescription");
        cardContent.put("trigger", "YOURMOVE");
        cardContent.put("advanced", "false");
        card = new Card(cardContent);
    }

    @Test
    void getName() {
        String name = card.getName();
        assertNotNull(name, "Card getName() returned null!");
        assertEquals(CardTest.name, name, "Card getName() does not match initializer intValueOf");
    }

    @Test
    void getId() {
        Integer id = card.getId();
        assertNotNull(id, "Card getId() returned null!");
        assertEquals(Integer.valueOf(CardTest.id), id, "Card getId() does not match initializer intValueOf");
    }

    @Test
    void getTitle() {
        String title = card.getTitle();
        assertNotNull(title, "Card getTitle() returned null!");
        assertEquals(CardTest.title, title, "Card getTitle() does not match initializer intValueOf");
    }

    @Test
    void getDescription() {
        String description = card.getDescription();
        assertNotNull(description, "Card getDescription() returned null!");
        assertEquals(CardTest.description, description, "Card getDescription() does not match initializer intValueOf");
    }

    @Test
    void getTrigger() {
        CardTrigger trigger = card.getTrigger();
        assertNotNull(trigger, "Card getTrigger() returned null!");
        assertEquals(CardTrigger.valueOf(CardTest.trigger), trigger, "Card getTrigger() does not match initializer intValueOf");
    }

    @Test
    void isAdvancedTest() {
        Boolean advanced = card.isAvanced();
        assertNotNull(advanced, "Card isAdvanced() returned null!");
        assertEquals(Boolean.parseBoolean(CardTest.advanced), advanced, "Card isAdvanced() does not match initializer intValueOf");
    }
}