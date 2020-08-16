package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import it.polimi.ingsw.santorini.model.gameoperations.exceptions.ApplicationAllowanceException;

import java.util.List;
import java.util.function.Function;

/**
 * Brief Expandable Function, it represents a special Function that can be expanded with more functions.
 * It executes first the expansive expansions and then the restrictive expansions distinguishing their effect on
 * the expandable by their expansion type
 * @param <T> The type of the Function input
 * @param <R> The type of the Function result
 * @param <S> The type of the supported expansions' marker
 * @param <A> The type of the supported accumulator
 * @see Function
 * @see AccumulatorExpandable
 */
public abstract class ExpFunction<T, R, S, A>
        extends AccumulatorExpandable<Function<T, R>, S, FunctionExpansion<T, R, S>, T, R, A>
        implements Function<T, R> {

    /**
     * Brief constructor that initializes the expandable and applicable state of the Expandable.
     * Resets also the filter's state of the expandable
     * */
    public ExpFunction() {
        super(true, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public R apply(T t) {
        requiresApplicable();
        resetAccumulator();
        applyExpansions(orderedExpansions(), t);
        return accumulatorToResult(accumulator);
    }

    /**
     * Brief Applies all the expansions previously added to the expandable given the input.
     * @param expansions The expansions being applied
     * @param input The input to be used on each expansion
     */
    private void applyExpansions(List<FunctionExpansion<T, R, S>> expansions, T input) throws ApplicationAllowanceException {
        applyExpansions(expansions,
                (expansion) -> accumulator = onUpdateAccumulatorRequested(expansion, accumulator, input));
    }
}
