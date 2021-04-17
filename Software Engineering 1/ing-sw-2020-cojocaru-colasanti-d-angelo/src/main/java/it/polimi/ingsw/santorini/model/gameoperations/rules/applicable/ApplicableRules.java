package it.polimi.ingsw.santorini.model.gameoperations.rules.applicable;

import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.rules.Rules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.RulesConvertible;
import it.polimi.ingsw.santorini.model.gameoperations.state.GameOperationState;

import java.util.function.Predicate;

/**
 * Brief Interface for the practical application of a certain type of Rules
 * @param <T> The type of the GameOperationState required for the rules application
 * @param <S> The type of the Rules that can be merged with this ApplicableRules
 * @see Rules
 */
public interface ApplicableRules<T extends GameOperationState, S extends Rules<T, S>> extends RulesConvertible<T, S> {
    /**
     * Brief State setter for the rules to use it as input at execution time
     * @param state The state being set
     */
    void setState(T state);

    /**
     * Brief Filter setter for the rules to be filtered at execution time
     * @param filter The filter being set
     */
    void setFilter(Predicate<GameMarker> filter);

    /**
     * Brief Resets the filter
     */
    void resetFilter();
}
