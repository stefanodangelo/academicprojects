package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import java.util.List;
import java.util.function.Function;

/**
 * Brief A ListFunction is a specialized Function that when applied, it returns a List as result
 * @param <T> The type of the ListFunction input
 * @param <E> The type of the ListFunction result's list element
 */
public interface ListFunction<T, E> extends Function<T, List<E>> {}
