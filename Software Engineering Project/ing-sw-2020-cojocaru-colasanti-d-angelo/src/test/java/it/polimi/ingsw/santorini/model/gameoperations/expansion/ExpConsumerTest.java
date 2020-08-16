package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import  it.polimi.ingsw.santorini.model.gameoperations.exceptions.MarkerLessExpansionAdded;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ExpConsumerTest {

    @Test
    void addExpansionTest() {
        MutableInteger test = new MutableInteger(0);
        ExpConsumer<MutableInteger, Object> expConsumer = new ExpConsumer<>();
        List<Object> markers = Collections.singletonList(new Object());
        Consumer<MutableInteger> consumer = (input) -> {input.plus(1);};

        expConsumer.addExpansion(new ConsumerExpansion<>(consumer, markers));
        expConsumer.accept(test);
        assertEquals(1, test.get(), "addExpansionTest ExpConsumer does not use properly the added expansion");
        assertEquals(false, expConsumer.isEmpty(), "addExpansionTest ExpConsumer still empty");

        assertThrows(MarkerLessExpansionAdded.class,
                () -> expConsumer.addExpansion(new ConsumerExpansion<>(consumer, new ArrayList<>())),
                "addExpansionTest MarkerLessExpansionAdded not thrown after adding expansion with empty markers list");

        expConsumer.addExpansion(new ConsumerExpansion<>(consumer, markers));
        expConsumer.addExpansion(new ConsumerExpansion<>(consumer, markers));
        assertEquals(3, expConsumer.expansions.size(),
                "addExpansionTest expansions list does not match the size expectations");
    }

    @Test
    void mergeTest() {
        MutableInteger test = new MutableInteger(0);
        ExpConsumer<MutableInteger, Object> expConsumer1 = new ExpConsumer<>();
        expConsumer1.addExpansion(new ConsumerExpansion<>((_test) -> _test.plus(1), new Object()));
        expConsumer1.accept(test);
        assertEquals(1, test.get(), "mergeTest expConsumer1 did not update correctly the test Integer");

        test.set(0);
        ExpConsumer<MutableInteger, Object> expConsumer2 = new ExpConsumer<>();
        expConsumer2.addExpansion(new ConsumerExpansion<>((_test) -> _test.plus(2), new Object()));
        expConsumer2.accept(test);
        assertEquals(2, test.get(), "mergeTest expConsumer2 did not update correctly the test Integer");

        test.set(0);
        expConsumer1.merge(expConsumer2);
        expConsumer1.accept(test);
        assertEquals(3, test.get(), "mergeTest merged consumers did not update correctly the test Integer");
    }

    @Test
    void isEmptyTest() {
        ExpConsumer<Object, Object> expConsumer = new ExpConsumer<>();
        assertEquals(true, expConsumer.isEmpty(), "isEmptyTest new ExpConsumer is not empty");

        List<Object> markers = Collections.singletonList(new Object());
        Consumer<Object> consumer = (input) -> {};
        expConsumer.addExpansion(new ConsumerExpansion<>(consumer, markers));
        assertEquals(false, expConsumer.isEmpty(), "isEmptyTest ExpConsumer that has one expansion is empty");
    }

    @Test
    void emptyTest() {
        ExpConsumer<Object, Object> expConsumer = new ExpConsumer<>();
        List<Object> markers = Collections.singletonList(new Object());
        Consumer<Object> consumer = (input) -> {};
        expConsumer.addExpansion(new ConsumerExpansion<>(consumer, markers));
        expConsumer.empty();
        assertEquals(true, expConsumer.isEmpty(), "emptyTest emptied ExpConsumer is not empty");
    }

    @Test
    void filterTest() {
        MutableInteger test = new MutableInteger(0);
        ExpConsumer<MutableInteger, Integer> expConsumer = new ExpConsumer<>();

        List<Integer> markers1 = Collections.singletonList(0);
        expConsumer.addExpansion(new ConsumerExpansion<>((_test) -> _test.plus(1), markers1));

        List<Integer> markers2 = Collections.singletonList(1);
        expConsumer.addExpansion(new ConsumerExpansion<>((_test) -> _test.plus(2), markers2));

        expConsumer.setFilter((marker) -> marker.equals(0));
        expConsumer.accept(test);
        assertEquals(1, test.get(), "filterTest expConsumer did not filter correctly");

        test.set(0);
        expConsumer.resetFilter();
        expConsumer.accept(test);
        assertEquals(3, test.get(), "filterTest expConsumer did not reset filter correctly");

        test.set(0);
        expConsumer.removeExpansionsByFilter((marker) -> marker.equals(0));
        expConsumer.accept(test);
        assertEquals(2, test.get(), "filterTest expConsumer did not remove expansions correctly");
    }

    @Test
    void acceptTest() {
        MutableInteger test = new MutableInteger(0);
        ExpConsumer<MutableInteger, Object> expConsumer = new ExpConsumer<>();
        expConsumer.addExpansion(new ConsumerExpansion<>((_test) -> _test.plus(1), new Object()));
        expConsumer.addExpansion(new ConsumerExpansion<>((_test) -> _test.plus(2), new Object()));
        expConsumer.accept(test);
        assertEquals(3, test.get(), "acceptTest expConsumer.accept() did not perform as expected");
    }
}