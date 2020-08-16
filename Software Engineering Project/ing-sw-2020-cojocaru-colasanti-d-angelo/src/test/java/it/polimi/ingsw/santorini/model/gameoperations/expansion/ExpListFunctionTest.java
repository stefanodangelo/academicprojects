package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import it.polimi.ingsw.santorini.model.gameoperations.exceptions.MarkerLessExpansionAdded;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ExpListFunctionTest {

    @Test
    void applyTest() {
        ExpListFunction<Object, Integer, Object> expListFunction = new ExpListFunction<>();
        expListFunction.addExpansion(new ListFunctionExpansion<>((input) -> Arrays.asList(0, 1), true, new Object()));
        expListFunction.addExpansion(new ListFunctionExpansion<>((input) -> Arrays.asList(2, 3), true, new Object()));
        expListFunction.addExpansion(new ListFunctionExpansion<>((input) -> Arrays.asList(1, 3), false, new Object()));
        List<Integer> result = expListFunction.apply(new Object());
        assertTrue(result.containsAll(new ArrayList<>(Arrays.asList(0, 2))),
                "applyTest expListFunction.apply() did not perform as expected");
    }

    @Test
    void addExpansionTest() {
        ExpListFunction<Object, Integer, Object> expListFunction = new ExpListFunction<>();
        List<Object> markers = Collections.singletonList(new Object());
        ListFunction<Object, Integer> listFunction = (input) -> Collections.singletonList(1);

        expListFunction.addExpansion(new ListFunctionExpansion<>(listFunction, true, markers));
        assertEquals(Collections.singletonList(1), expListFunction.apply(new Object()),
                "addExpansionTest expListFunction does not use properly the added expansion");
        assertEquals(false, expListFunction.isEmpty(), "addExpansionTest expListFunction still empty");

        assertThrows(MarkerLessExpansionAdded.class,
                () -> expListFunction.addExpansion(new ListFunctionExpansion<>(listFunction, true, new ArrayList<>())),
                "addExpansionTest MarkerLessExpansionAdded not thrown after adding expansion with empty markers list");

        expListFunction.addExpansion(new ListFunctionExpansion<>(listFunction, false, markers));
        expListFunction.addExpansion(new ListFunctionExpansion<>(listFunction, false, markers));
        assertTrue(Helper.getExpansiveSize(expListFunction).equals(1) && Helper.getRestrictiveSize(expListFunction).equals(2),
                "addExpansionTest expansive and restrictive lists does not match the size expectations");
    }

    @Test
    void mergeTest() {
        ExpListFunction<Object, Integer, Object> expListFunction1 = new ExpListFunction<>();
        expListFunction1.addExpansion(new ListFunctionExpansion<>((input) -> Arrays.asList(0, 1), true, new Object()));
        assertEquals(Arrays.asList(0, 1), expListFunction1.apply(new Object()),
                "mergeTest expListFunction1 did not update correctly the test Integer");

        ExpListFunction<Object, Integer, Object> expListFunction2 = new ExpListFunction<>();
        expListFunction2.addExpansion(new ListFunctionExpansion<>((input) -> Arrays.asList(2, 3), true, new Object()));
        assertEquals(Arrays.asList(2, 3), expListFunction2.apply(new Object()),
                "mergeTest expListFunction2 did not update correctly the test Integer");

        expListFunction1.merge(expListFunction2);
        List<Integer> result = expListFunction1.apply(new Object());
        assertTrue(result.containsAll(new ArrayList<>(Arrays.asList(0, 1, 2, 3))),
                "mergeTest merged consumers did not update correctly the test Integer");
    }

    @Test
    void isEmptyTest() {
        ExpListFunction<Object, Object, Object> expListFunction = new ExpListFunction<>();
        assertEquals(true, expListFunction.isEmpty(), "isEmptyTest new ExpListFunction is not empty");

        List<Object> markers = Collections.singletonList(new Object());
        ListFunction<Object, Object> listFunction = (input) -> null;
        expListFunction.addExpansion(new ListFunctionExpansion<>(listFunction, true, markers));
        assertEquals(false, expListFunction.isEmpty(),
                "isEmptyTest ExpListFunction that has one expansion is empty");
    }

    @Test
    void emptyTest() {
        ExpListFunction<Object, Object, Object> expListFunction = new ExpListFunction<>();
        List<Object> markers = Collections.singletonList(new Object());
        ListFunction<Object, Object> listFunction = (input) -> null;
        expListFunction.addExpansion(new ListFunctionExpansion<>(listFunction, true, markers));
        expListFunction.empty();
        assertEquals(true, expListFunction.isEmpty(), "emptyTest emptied ExpListFunction is not empty");
    }

    @Test
    void filterTest() {
        ExpListFunction<Object, Integer, Integer> expListFunction = new ExpListFunction<>();

        List<Integer> markers1 = Collections.singletonList(0);
        expListFunction.addExpansion(new ListFunctionExpansion<>((input) -> Arrays.asList(0, 1), true, markers1));

        List<Integer> markers2 = Collections.singletonList(1);
        expListFunction.addExpansion(new ListFunctionExpansion<>((input) -> Arrays.asList(2, 3), true, markers2));

        expListFunction.setFilter((marker) -> marker.equals(0));
        List<Integer> result = expListFunction.apply(new Object());
        assertTrue(result.containsAll(new ArrayList<>(Arrays.asList(0,1))),
                "filterTest expListFunction did not filter correctly");

        expListFunction.resetFilter();
        result = expListFunction.apply(new Object());
        assertTrue(result.containsAll(new ArrayList<>(Arrays.asList(0,1,2,3))),
                "filterTest expListFunction did not reset filter correctly");

        expListFunction.removeExpansionsByFilter((marker) -> marker.equals(0));
        assertTrue(result.containsAll(new ArrayList<>(Arrays.asList(2,3))),
                "filterTest expListFunction did not remove expansions correctly");
    }

    @Test
    void onUpdateAccumulatorRequestedTest() {
        ExpListFunction<Object, Integer, Object> expListFunction = new ExpListFunction<>();

        Set<Integer> accumulator = new HashSet<>(Arrays.asList(0, 1));

        accumulator = expListFunction.onUpdateAccumulatorRequested(new ListFunctionExpansion<>(
                (input) -> new ArrayList<>(Arrays.asList(2, 3)), true, new Object()), accumulator,
                new Object());
        assertTrue(accumulator.containsAll(Arrays.asList(0, 1, 2, 3)),
                "onUpdateAccumulatorRequestedTest accumulator test 1 failed");

        accumulator = expListFunction.onUpdateAccumulatorRequested(new ListFunctionExpansion<>(
                        (input) -> new ArrayList<>(Arrays.asList(0, 2)), false, new Object()), accumulator,
                new Object());
        assertTrue(accumulator.containsAll(Arrays.asList(1, 3)),
                "onUpdateAccumulatorRequestedTest accumulator test 2 failed");
    }

    @Test
    void accumulatorToResultTest() {
        ExpListFunction<Object, Integer, Object> expListFunction = new ExpListFunction<>();
        List<Integer> result = expListFunction.accumulatorToResult(new HashSet<>(new ArrayList<>(Arrays.asList(0, 1, 2))));
        assertTrue(result.containsAll(new ArrayList<>(Arrays.asList(0, 1, 2))),
                "accumulatorToResultTest accumulator to result has returned wrong result");
    }

    @Test
    void refreshedAccumulatorTest() {
        ExpListFunction<Object, Integer, Object> expListFunction = new ExpListFunction<>();
        assertTrue(expListFunction.refreshedAccumulator().isEmpty(),
                "refreshedAccumulatorTest the provided accumulator is not the right one");
    }
}