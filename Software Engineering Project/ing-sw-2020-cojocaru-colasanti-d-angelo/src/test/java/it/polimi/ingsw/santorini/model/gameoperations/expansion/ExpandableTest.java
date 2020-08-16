package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import it.polimi.ingsw.santorini.model.gameoperations.exceptions.ApplicationAllowanceException;
import it.polimi.ingsw.santorini.model.gameoperations.exceptions.ExpansionAllowanceException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ExpandableTest {

    @Test
    void permissionsTest() {
        ExpPredicate<Object, Object> expPredicate = new ExpPredicate<>();
        assertTrue(expPredicate.isApplicable() && expPredicate.isExpandable(), "permissions error");
        expPredicate.setPermissions(true, true);
        expPredicate.setApplicable(false);
        assertDoesNotThrow(() -> expPredicate.addExpansion(new PredicateExpansion<>(obj -> true, false, new Object())));
        expPredicate.setApplicable(true);
        expPredicate.setExpandable(false);
        assertDoesNotThrow(() -> expPredicate.test(null));
        expPredicate.setPermissions(false, false);
        assertThrows(ApplicationAllowanceException.class, () -> expPredicate.test(null));
    }

    @Test
    void expFunctionTest() {
        ExpFunction<Integer, List<Integer>, Integer, Set<Integer>> expFunction = new ExpListFunction<>();
        assertTrue(expFunction.isExpandable() && expFunction.isApplicable());
        expFunction.setExpandable(true);
        expFunction.addExpansion(new FunctionExpansion<>(integer -> new ArrayList<>(Arrays.asList(0, 1, 2))
                , true, new ArrayList<>(Arrays.asList(0, 1, 2))));
        expFunction.empty();
        assertEquals(0, (int) expFunction.size(), "Empty error");
        expFunction.addExpansion(new FunctionExpansion<>(integer -> new ArrayList<>(Arrays.asList(0, 1))
                , true, new ArrayList<>(Arrays.asList(0, 1, 8))));
        expFunction.addExpansion(new FunctionExpansion<>(integer -> new ArrayList<>(Arrays.asList(0, 1, 9))
                , true, new ArrayList<>(Arrays.asList(4, 7, -1))));
        expFunction.addExpansion(new FunctionExpansion<>(integer -> new ArrayList<>(Arrays.asList(0, 0))
                , false, new ArrayList<>(Arrays.asList(0, 1, 2))));
        expFunction.addExpansion(new FunctionExpansion<>(integer -> new ArrayList<>(Arrays.asList(0, 3))
                , true, new ArrayList<>(Arrays.asList(7, 4))));
        expFunction.removeExpansionsByFilter(marker -> marker > 2);
        expFunction.overwriteExpansion(new FunctionExpansion<>(integer -> new ArrayList<>(Arrays.asList(0, 1, 2))
                , false, new ArrayList<>(Arrays.asList(0, 1))), 0);
        expFunction.removeExpansion(1);
        expFunction.addExpansionFirst(new FunctionExpansion<>(integer -> new ArrayList<>(Arrays.asList(0, 1, 2, 8))
                , true, new ArrayList<>(Arrays.asList(0, 1, 5))));
        expFunction.addExpansion(new FunctionExpansion<>(integer -> new ArrayList<>(Arrays.asList(0, 1, 7, 2))
                , true, new ArrayList<>(Arrays.asList(0, 1, 7))));
        expFunction.setFilter(marker -> marker < 2);

        List<Integer> result = Arrays.asList(7, 8);
        assertEquals(result, expFunction.apply(0));
    }

    @Test
    void expPredicateTest() {

    }
}
