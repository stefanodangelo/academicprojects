package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Brief Expandable ListFunction, it represents a special ListFunction that can be expanded with more functions.
 * It executes first the expansive expansions and then the restrictive expansions distinguishing their effect on
 * the expandable by their expansion type
 * @param <T> The type of the ListFunction input
 * @param <E> The type of the ListFunction result's list element
 * @param <S> The type of the supported expansions' marker
 * @see ListFunction
 * @see ExpFunction
 */
public class ExpListFunction<T, E, S> extends ExpFunction<T, List<E>, S, Set<E>> {

    /**
     * Brief allows expandable and denies applicable
     * @return ExpListFunction this
     */
    public ExpListFunction<T, E, S> asExpandableOnly() {
        setPermissions(true,false);
        return this;
    }

    /**
     * Brief allows applicable and denies expandable
     * @return ExpListFunction this
     */
    public ExpListFunction<T, E, S> asApplicableOnly() {
        setPermissions(false,true);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Set<E> onUpdateAccumulatorRequested(FunctionExpansion<T, List<E>, S> expansion, Set<E> accumulator, T input) {
        List<E> partialResult = expansion.getObject().apply(input);
        if (expansion.getType().equals(ExpansionType.EXPANSIVE))
            accumulator.addAll(partialResult);
        else
            accumulator.removeAll(partialResult);
        return accumulator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<E> accumulatorToResult(Set<E> accumulator) {
        return new ArrayList<>(accumulator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Set<E> refreshedAccumulator() {
        return new HashSet<>();
    }
}
