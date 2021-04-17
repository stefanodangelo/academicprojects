package it.polimi.ingsw.santorini.model.gameoperations;

import it.polimi.ingsw.santorini.model.gameoperations.rules.expandable.ExpandableRulesContainer;

/**
 * Brief Interface that allows to expand some stored rules
 * @see it.polimi.ingsw.santorini.model.gameoperations.rules.Rules
 */
public interface ExpandableGameOperation {
    /**
     * Brief Method that is responsible of expanding the current rules using expansions
     * delivered as ExpandableRulesContainer
     * @param rules ExpandableRulesContainer the incoming expansions' container
     */
    void expandRules(ExpandableRulesContainer rules);
}
