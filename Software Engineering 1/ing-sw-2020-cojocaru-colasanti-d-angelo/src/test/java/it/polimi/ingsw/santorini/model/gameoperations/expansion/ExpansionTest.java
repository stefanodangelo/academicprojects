package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class ExpansionTest {

    @Test
    void hasTypeTest() {
        final List<Object> markers = Collections.singletonList(new Object());

        Expansion<Object, Object> expansionWithType = new Expansion<>(new Object(), true, markers);
        assertEquals(true, expansionWithType.hasType(), "hasTypeTest expansionWithType actually has not type");

        Expansion<Object, Object> expansionNoType = new Expansion<>(new Object(), markers);
        assertEquals(false, expansionNoType.hasType(), "hasTypeTest expansionNoType actually has type");
    }

    @Test
    void expansionInstance() {
        Integer content1 = 1;
        ExpansionType type1 = ExpansionType.EXPANSIVE;
        List<Integer> markers1 = Arrays.asList(0, 1, 2);
        Expansion<Integer, Integer> expansion1 = new Expansion<>(content1, type1, markers1);
        assertSame(content1, expansion1.getObject(), "instantiation error");
        assertEquals(ExpansionType.EXPANSIVE, expansion1.getType(), "instantiation error");
        assertEquals(markers1, expansion1.getMarkers(), "instantiation");

        Integer content2 = 1;
        List<Integer> markers2 = Arrays.asList(0, 1, 2);
        Expansion<Integer, Integer> expansion2 = new Expansion<>(content2, true, markers2);
        assertSame(content2, expansion2.getObject(), "instantiation error");
        assertEquals(ExpansionType.EXPANSIVE, expansion2.getType(), "instantiation error");
        assertEquals(markers2, expansion2.getMarkers(), "instantiation");

        Integer content3 = 1;
        ExpansionType type3 = ExpansionType.RESTRICTIVE;
        List<Integer> markers3 = Arrays.asList(0, 1, 2);
        Expansion<Integer, Integer> expansion3 = new Expansion<>(content3, type3, markers3);
        assertSame(content3, expansion3.getObject(), "instantiation error");
        assertEquals(ExpansionType.RESTRICTIVE, expansion3.getType(), "instantiation error");
        assertEquals(markers3, expansion3.getMarkers(), "instantiation");

        Integer content4 = 1;
        List<Integer> markers4 = Arrays.asList(0, 1, 2);
        Expansion<Integer, Integer> expansion4 = new Expansion<>(content2, false, markers2);
        assertSame(content4, expansion4.getObject(), "instantiation error");
        assertEquals(ExpansionType.RESTRICTIVE, expansion4.getType(), "instantiation error");
        assertEquals(markers4, expansion4.getMarkers(), "instantiation");
    }

    @Test
    void consumerExpansionInstance() {
        Consumer<Integer> content1 = integer -> {};
        List<Integer> markers1 = Arrays.asList(0, 1, 2);
        ConsumerExpansion<Integer, Integer> expansion1 = new ConsumerExpansion<>(content1, markers1);
        assertSame(content1, expansion1.getObject(), "instantiation error");
        assertNull(expansion1.getType(), "instantiation error");
        assertEquals(markers1, expansion1.getMarkers(), "instantiation");
    }

    @Test
    void functionExpansionInstance() {
        Function<Integer, Integer> content1 = integer -> 1;
        List<Integer> markers1 = Arrays.asList(0, 1, 2);
        FunctionExpansion<Integer, Integer, Integer> expansion1 = new FunctionExpansion<>(content1, true, markers1);
        assertSame(content1, expansion1.getObject(), "instantiation error");
        assertEquals(ExpansionType.EXPANSIVE, expansion1.getType(), "instantiation error");
        assertEquals(markers1, expansion1.getMarkers(), "instantiation");
    }

    @Test
    void listFunctionExpansionInstance() {
        ListFunction<Integer, Integer> content1 = integer -> new ArrayList<>();
        List<Integer> markers1 = Arrays.asList(0, 1, 2);
        ListFunctionExpansion<Integer, Integer, Integer> expansion1 = new ListFunctionExpansion<>(content1, true, markers1);
        assertSame(content1, expansion1.getObject(), "instantiation error");
        assertEquals(ExpansionType.EXPANSIVE, expansion1.getType(), "instantiation error");
        assertEquals(markers1, expansion1.getMarkers(), "instantiation");
    }

    @Test
    void predicateExpansionInstance() {
        Predicate<Boolean> content1 = bool -> bool;
        List<Integer> markers1 = Arrays.asList(0, 1, 2);
        PredicateExpansion<Boolean, Integer> expansion1 = new PredicateExpansion<>(content1, true, markers1);
        assertSame(content1, expansion1.getObject(), "instantiation error");
        assertEquals(ExpansionType.EXPANSIVE, expansion1.getType(), "instantiation error");
        assertEquals(markers1, expansion1.getMarkers(), "instantiation");
    }
}