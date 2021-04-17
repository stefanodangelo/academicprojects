package it.polimi.ingsw.santorini.model.gameoperations.rules;


import it.polimi.ingsw.santorini.model.gameoperations.state.GameOperationState;

/**
 * Brief Interface for Rules conversion
 * @param <T> The type of the GameOperationState required for the rules application
 * @param <S> The type of the Rules that can be merged with this RulesConvertible
 *
 */
public interface RulesConvertible<T extends GameOperationState, S extends Rules<T, S>> {
    /**
     * Brief Converts this to a Rules instance
     * @return S Rules instance
     */
    S toRules();

    /**
     * Brief Merges this RulesConvertible with another one
     * @param rulesConvertible the RulesConvertible being merged
     */
    default void merge(RulesConvertible<T, S> rulesConvertible) {
        toRules().merge(rulesConvertible.toRules());
    }

    /**
     * Brief Tells if this instance is rules complete
     * @return Boolean true if at least one expansion per rule is found, false otherwise
     */
    Boolean isComplete();
}
