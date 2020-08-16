package it.polimi.ingsw.santorini.model.gameoperations.rules.expandable;


import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.Expansion;
import it.polimi.ingsw.santorini.model.gameoperations.rules.BRules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.MRules;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Brief ExpandableRules container that is able to expand both Move and Build rules
 * @see MRules
 * @see BRules
 */
public class ExpandableRulesContainer {

    /**
     * Brief The expandable MRules instance
     */
    private final ExpandableMRules mRules = new MRules(false);

    /**
     * Brief The expandable BRules instance
     */
    private final ExpandableBRules bRules = new BRules(false);

    /**
     * Brief Getter fo the MRules instance
     * @return ExpandableMRules expandable MRules
     */
    public ExpandableMRules mRules() {
        return mRules;
    }

    /**
     * Brief Getter fo the BRules instance
     * @return ExpandableBRules expandable BRules
     */
    public ExpandableBRules bRules() {
        return bRules;
    }

    /**
     * Brief Merges this container with another one
     * @param container ExpandableRulesContainer the container being merged
     */
    public void merge(ExpandableRulesContainer container) {
        mRules.merge(container.mRules);
        bRules.merge(container.bRules);
    }

    /**
     * Brief Removes all the undesired expansions from all expandable rules in both instances
     * @param undesired Predicate the undesired filter
     */
    public void removeExpansionsByFilter(Predicate<GameMarker> undesired) {
        mRules.toRules().removeExpansionsByFilter(undesired);
        bRules.toRules().removeExpansionsByFilter(undesired);
    }

    /**
     * Brief Returns all the Expansions contained across the stored Expandable Rules
     * @return List all the expansions
     */
    public List<Expansion<?,GameMarker>> getAllExpansions() {
        List<Expansion<?,GameMarker>> expansions = new ArrayList<>();
        expansions.addAll(mRules.getAllExpansions());
        expansions.addAll(bRules.getAllExpansions());
        return expansions;
    }
}
