package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import it.polimi.ingsw.santorini.model.gameoperations.exceptions.ApplicationAllowanceException;

import java.util.List;
import java.util.function.Predicate;

/**
 * Brief Expandable Predicate, it represents a special Predicate that can be expanded with more functions.
 * It executes first the expansive expansions and then the restrictive expansions distinguishing their effect on
 * the expandable by their expansion type
 * @param <T> The type of the Predicate input
 * @param <S> The type of the supported expansions' marker
 * @see Predicate
 * @see AccumulatorExpandable
 */
public class ExpPredicate<T, S>
        extends AccumulatorExpandable<Predicate<T>, S, PredicateExpansion<T, S>, T, Boolean, Boolean>
        implements Predicate<T> {

    /**
     * Brief constructor that initializes the expandable and applicable state of the Expandable.
     * Resets also the filter's state of the expandable
     */
    public ExpPredicate() {
        super(true, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean test(T t) {
        requiresApplicable();
        resetAccumulator();
        applyExpansions(orderedExpansions(), t);
        return accumulatorToResult(accumulator);
    }

    /**
     * Brief allows expandable and denies applicable
     * @return ExpPredicate this
     */
    public ExpPredicate<T, S> asExpandableOnly() {
        setPermissions(true,false);
        return this;
    }

    /**
     * Brief allows applicable and denies expandable
     * @return ExpPredicate this
     */
    public ExpPredicate<T, S> asApplicableOnly() {
        setPermissions(false,true);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean onUpdateAccumulatorRequested(PredicateExpansion<T, S> expansion, Boolean accumulator, T input) {
        boolean partialResult = expansion.getObject().test(input);
        if (expansion.getType().equals(ExpansionType.EXPANSIVE))
            accumulator = Boolean.logicalOr(accumulator, partialResult);
        else
            accumulator = Boolean.logicalAnd(accumulator, partialResult);
        return accumulator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean accumulatorToResult(Boolean accumulator) {
        return accumulator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean refreshedAccumulator() {
        return Boolean.FALSE;
    }

    /**
     * Brief Applies all the expansions previously added to the expandable given the input.
     * @param expansions The expansions being applied
     * @param input The input to be used on each expansion
     */
    private void applyExpansions(List<PredicateExpansion<T, S>> expansions, T input) throws ApplicationAllowanceException {
        applyExpansions(expansions, (expansion) ->
                accumulator = onUpdateAccumulatorRequested(expansion, accumulator, input));
    }
}
