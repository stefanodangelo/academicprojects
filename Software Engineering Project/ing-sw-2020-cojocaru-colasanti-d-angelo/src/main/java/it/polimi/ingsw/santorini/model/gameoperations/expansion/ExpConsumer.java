package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import it.polimi.ingsw.santorini.model.gameoperations.exceptions.ApplicationAllowanceException;

import java.util.List;
import java.util.function.Consumer;

/**
 * Brief Expandable Consumer, it represents a special Consumer that can be expanded with more consumers.
 * It executes the expansions in the given order and not distinguishing them by ExpansionType
 * @param <T> The Consumer's input type
 * @param <S> The type of the supported expansions' marker
 * @see Consumer
 * @see SimpleExpandable
 */
public class ExpConsumer<T, S>
        extends SimpleExpandable<Consumer<T>, S, ConsumerExpansion<T, S>> implements Consumer<T> {

    /**
     * Brief constructor that initializes the expandable and applicable state of the Expandable.
     * Resets also the filter's state of the expandable
     */
    public ExpConsumer() {
        super(true, true);
    }

    /**
     * Brief allows expandable and denies applicable
     * @return ExpConsumer this
     */
    public ExpConsumer<T, S> asExpandableOnly() {
        setPermissions(true,false);
        return this;
    }

    /**
     * Brief allows applicable and denies expandable
     * @return ExpConsumer this
     */
    public ExpConsumer<T, S> asApplicableOnly() {
        setPermissions(false,true);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(T t) {
        requiresApplicable();
        applyExpansions(expansions, t);
    }

    /**
     * Brief Applies all the expansions previously added to the expandable given the input.
     * @param expansions The expansions being applied
     * @param input The input to be used on each expansion
     */
    private void applyExpansions(List<ConsumerExpansion<T, S>> expansions, T input) throws ApplicationAllowanceException {
        applyExpansions(expansions, (expansion) -> expansion.getObject().accept(input));
    }
}
