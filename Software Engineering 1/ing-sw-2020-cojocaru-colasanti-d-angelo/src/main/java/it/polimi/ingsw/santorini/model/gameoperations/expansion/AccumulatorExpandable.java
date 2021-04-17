package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a complex expandable that makes use of an accumulator and of inputs
 * to manifest his behaviour based on expansions. It also produces a concrete result through the accumulator.
 * @param <T> The type of the object that refines the expandable's behaviour
 * @param <S> The type of the supported expansions' marker
 * @param <U> The type of supported expansions
 * @param <V> The type of the input
 * @param <R> The type of the result
 * @param <A> The type of the accumulator
 */
public abstract class AccumulatorExpandable<T, S, U extends Expansion<T, S>, V, R, A>
        extends SimpleExpandable<T, S, U> {

    /**
     * Brief The accumulator used to compute the result expansion after expansion
     */
    protected A accumulator = refreshedAccumulator();

    /**
     * Brief Resets the accumulator
     */
    protected void resetAccumulator() {
        accumulator = refreshedAccumulator();
    }

    /**
     * Brief constructor that initializes the expandable and applicable state of the Expandable.
     * Resets also the filter's state of the expandable
     *
     * @param expandable Boolean true if expansions allowed
     * @param applicable Boolean true if applications on expansions allowed
     */
    public AccumulatorExpandable(Boolean expandable, Boolean applicable) {
        super(expandable, applicable);
    }

    /**
     * Brief Updates the accumulator based on an expansion and an input
     * @param expansion The involved expansion
     * @param accumulator The involved accumulator
     * @param input The involved input
     * @return A the updated accumulator
     */
    protected abstract A onUpdateAccumulatorRequested(U expansion, A accumulator, V input);

    /**
     * Brief It converts the accumulator in a concrete result
     * @param accumulator The involved accumulator
     * @return R the result extracted from the accumulator
     */
    protected abstract R accumulatorToResult(A accumulator);

    /**
     * Brief It provides a fresh and ready to use accumulator
     * @return A the fresh accumulator
     */
    protected abstract A refreshedAccumulator();

    /**
     * Brief It provides an ordered expansion list made from expansions variable.
     * The order is computed in that way: ExpansionType.EXPANSIVE is less than ExpansionType.Restrictive
     * @return the ordered list of expansions
     */
    protected List<U> orderedExpansions() {
        return expansions.stream().sorted((o1, o2) -> {
            if (o1.getType() .equals(o2.getType())) return 0;
            else if(o1.getType().equals(ExpansionType.EXPANSIVE)) return -1;
            else return +1;
        }).collect(Collectors.toList());
    }
}
