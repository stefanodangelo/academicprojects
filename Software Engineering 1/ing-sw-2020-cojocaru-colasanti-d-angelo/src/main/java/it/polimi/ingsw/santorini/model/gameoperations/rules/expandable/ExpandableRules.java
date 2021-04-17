package it.polimi.ingsw.santorini.model.gameoperations.rules.expandable;

import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.Expansion;
import it.polimi.ingsw.santorini.model.gameoperations.rules.Rules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.RulesConvertible;
import it.polimi.ingsw.santorini.model.gameoperations.state.GameOperationState;

import java.util.List;
import java.util.function.Predicate;

/**
 * Brief Interface for the expansion of a certain type of Rules
 * @param <T> The type of the GameOperationState required for the rules application
 * @param <S> The type of the Rules that can be merged with this ExpandableRules
 * @see Rules
 */
public interface ExpandableRules<T extends GameOperationState, S extends Rules<T, S>> extends RulesConvertible<T, S> {
    /**
     * Brief It completes the empty rules with the default rules' expansions
     */
    void completeRules();

    /**
     * Brief It loads the default rules' expansions
     */
    void loadDefaultRules();

    /**
     * Brief Removes all the undesired expansions from all expandable rules
     * @param undesired Predicate the undesired filter
     */
    void removeExpansionsByFilter(Predicate<GameMarker> undesired);

    /**
     * Brief Returns all the stored expansions within these rules
     * @return List the list of expansions
     */
    List<Expansion<?,GameMarker>> getAllExpansions();
}
