package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import it.polimi.ingsw.santorini.model.gameoperations.exceptions.MarkerLessExpansionAdded;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class ExpPredicateTest {

    @Test
    void addExpansionTest() {
        ExpPredicate<Object, Object> expPredicate = new ExpPredicate<>();
        List<Object> markers = Collections.singletonList(new Object());
        Predicate<Object> predicate = (input) -> true;

        expPredicate.addExpansion(new PredicateExpansion<>(predicate, true, markers));
        assertTrue(expPredicate.test(new Object()), "addExpansionTest expPredicate does not use properly the added expansion");
        assertEquals(false, expPredicate.isEmpty(), "addExpansionTest ExpConsumer still empty");

        assertThrows(MarkerLessExpansionAdded.class,
                () -> expPredicate.addExpansion(new PredicateExpansion<>(predicate, true, new ArrayList<>())),
                "addExpansionTest MarkerLessExpansionAdded not thrown after adding expansion with empty markers list");

        expPredicate.addExpansion(new PredicateExpansion<>(predicate, false, markers));
        expPredicate.addExpansion(new PredicateExpansion<>(predicate, false, markers));
        assertTrue(Helper.getExpansiveSize(expPredicate).equals(1) && Helper.getRestrictiveSize(expPredicate).equals(2),
                "addExpansionTest expansive and restrictive lists does not match the size expectations");
    }

    @Test
    void mergeTest() {
        ExpPredicate<Object, Object> expPredicate1 = new ExpPredicate<>();
        expPredicate1.addExpansion(new PredicateExpansion<>((input) -> true, true, new Object()));
        assertTrue(expPredicate1.test(new Object()), "mergeTest expPredicate1 did not update correctly the test Integer");

        ExpPredicate<Object, Object> expPredicate2 = new ExpPredicate<>();
        expPredicate2.addExpansion(new PredicateExpansion<>((input) -> false, false, new Object()));
        assertFalse(expPredicate2.test(new Object()), "mergeTest expPredicate2 did not update correctly the test Integer");

        expPredicate1.merge(expPredicate2);
        assertFalse(expPredicate1.test(new Object()), "mergeTest merged consumers did not update correctly the test Integer");
    }

    @Test
    void isEmptyTest() {
        ExpPredicate<Object, Object> expPredicate = new ExpPredicate<>();
        assertEquals(true, expPredicate.isEmpty(), "isEmptyTest new ExpPredicate is not empty");

        List<Object> markers = Collections.singletonList(new Object());
        Predicate<Object> predicate = (input) -> true;
        expPredicate.addExpansion(new PredicateExpansion<>(predicate, true, markers));
        assertEquals(false, expPredicate.isEmpty(), "isEmptyTest ExpPredicate that has one expansion is empty");
    }

    @Test
    void emptyTest() {
        ExpPredicate<Object, Object> expPredicate = new ExpPredicate<>();
        List<Object> markers = Collections.singletonList(new Object());
        Predicate<Object> predicate = (input) -> true;
        expPredicate.addExpansion(new PredicateExpansion<>(predicate, true, markers));
        expPredicate.empty();
        assertEquals(true, expPredicate.isEmpty(), "emptyTest emptied ExpPredicate is not empty");
    }

    @Test
    void filterTest() {
        ExpPredicate<Object, Integer> expPredicate = new ExpPredicate<>();

        List<Integer> markers1 = Collections.singletonList(0);
        expPredicate.addExpansion(new PredicateExpansion<>((input) -> true, true, markers1));

        List<Integer> markers2 = Collections.singletonList(1);
        expPredicate.addExpansion(new PredicateExpansion<>((input) -> false, false, markers2));

        expPredicate.setFilter((marker) -> marker.equals(0));
        assertTrue(expPredicate.test(new Object()), "filterTest expPredicate did not filter correctly");

        expPredicate.resetFilter();
        assertFalse(expPredicate.test(new Object()), "filterTest expPredicate did not reset filter correctly");

        expPredicate.removeExpansionsByFilter((marker) -> marker.equals(0));
        assertFalse(expPredicate.test(new Object()), "filterTest expPredicate did not remove expansions correctly");
    }

    @Test
    void testTest() {
        ExpPredicate<Object, Object> expPredicate = new ExpPredicate<>();
        expPredicate.addExpansion(new PredicateExpansion<>((input) -> true, true, new Object()));
        expPredicate.addExpansion(new PredicateExpansion<>((input) -> false, true, new Object()));
        expPredicate.addExpansion(new PredicateExpansion<>((input) -> true, false, new Object()));
        expPredicate.addExpansion(new PredicateExpansion<>((input) -> false, false, new Object()));
        assertFalse(expPredicate.test(new Object()), "testTest expPredicate.test() did not perform as expected");
    }

    @Test
    void onUpdateAccumulatorRequestedTest() {
        ExpPredicate<Object, Object> expPredicate = new ExpPredicate<>();
        Predicate<Object> truePredicate = (input) -> true;
        Predicate<Object> falsePredicate = (input) -> false;

        assertTrue(expPredicate.onUpdateAccumulatorRequested(
                new PredicateExpansion<>(falsePredicate, true, new Object()), true, new Object()),
                "onUpdateAccumulatorRequestedTest accumulator test 1 failed");

        assertFalse(expPredicate.onUpdateAccumulatorRequested(
                new PredicateExpansion<>(falsePredicate, true, new Object()), false, new Object()),
                "onUpdateAccumulatorRequestedTest accumulator test 2 failed");

        assertTrue(expPredicate.onUpdateAccumulatorRequested(
                new PredicateExpansion<>(truePredicate, false, new Object()), true, new Object()),
                "onUpdateAccumulatorRequestedTest accumulator test 3 failed");

        assertFalse(expPredicate.onUpdateAccumulatorRequested(
                new PredicateExpansion<>(truePredicate, false, new Object()), false, new Object()),
                "onUpdateAccumulatorRequestedTest accumulator test 4 failed");
    }

    @Test
    void accumulatorToResultTest() {
        ExpPredicate<Object, Object> expPredicate = new ExpPredicate<>();
        assertTrue(expPredicate.accumulatorToResult(true),
                "accumulatorToResultTest accumulator to result has returned wrong result");
        assertFalse(expPredicate.accumulatorToResult(false),
                "accumulatorToResultTest accumulator to result has returned wrong result");
    }

    @Test
    void refreshedAccumulatorTest() {
        ExpPredicate<Object, Object> expPredicate = new ExpPredicate<>();
        assertEquals(false, expPredicate.refreshedAccumulator(),
                "refreshedAccumulatorTest the provided accumulator is not the right one");
    }
}