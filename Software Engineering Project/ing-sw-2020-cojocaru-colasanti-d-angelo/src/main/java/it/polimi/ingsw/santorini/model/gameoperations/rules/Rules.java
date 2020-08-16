package it.polimi.ingsw.santorini.model.gameoperations.rules;

import it.polimi.ingsw.santorini.model.gameoperations.GameMarker;
import it.polimi.ingsw.santorini.model.gameoperations.exceptions.ExpansionAllowanceException;
import it.polimi.ingsw.santorini.model.gameoperations.exceptions.RulesStateNotSetException;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.Expandable;
import it.polimi.ingsw.santorini.model.gameoperations.expansion.Expansion;
import it.polimi.ingsw.santorini.model.gameoperations.rules.applicable.ApplicableRules;
import it.polimi.ingsw.santorini.model.gameoperations.rules.expandable.ExpandableRules;
import it.polimi.ingsw.santorini.model.gameoperations.state.GameOperationState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Brief Represents the game rules handler. It uses a GameOperationState to execute the different rules it may store.
 * It can be merged with other supported Rules. The rules stored within must be both applicable and expandable
 * @param <T> The type of the supported GameOperationState in order to execute the rules
 * @param <S> The type of the supported Rules that can be merged with
 * @see GameOperationState
 */
public abstract class Rules<T extends GameOperationState, S extends Rules<T, S>>
        implements ApplicableRules<T, S>, ExpandableRules<T, S> {

    /**
     * Brief The GameOperationState state for rules' execution
     */
    private T state;

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isComplete() {
        return getExpandableList().stream().noneMatch(Expandable::isEmpty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void loadDefaultRules();

    /**
     * Brief Getter for the state
     * @return T the state
     * @throws RulesStateNotSetException if the state is null for the moment
     */
    public T getState() throws RulesStateNotSetException {
        if (state == null) throw new RulesStateNotSetException();
        return state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(T state) {
        this.state = state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFilter(Predicate<GameMarker> filter) {
        getExpandableList().forEach((expandable) -> expandable.setFilter(filter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetFilter() {
        getExpandableList().forEach(Expandable::resetFilter);
    }

    /**
     * Brief Removes the undesired expansions from all expandable rules that are stored within
     * @param undesired The undesired filter involved
     */
    public void removeExpansionsByFilter(Predicate<GameMarker> undesired) {
        getExpandableList().forEach((expandable) -> {
            try {
                expandable.removeExpansionsByFilter(undesired);
            } catch (ExpansionAllowanceException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Brief Merges all the expandable rules with all the respective expandable rules from the parameter
     * @param rules Rules that are being merged
     */
    public abstract void merge(S rules);

    /**
     * Brief Provides all expandable rules stored as a handy list
     * @return the list of all expandable rules available
     */
    protected abstract List<Expandable<?, GameMarker, ?, ?>> getExpandableList();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Expansion<?,GameMarker>> getAllExpansions() {
        List<Expansion<?,GameMarker>> expansions = new ArrayList<>();
        getExpandableList().forEach(expandable -> expansions.addAll(expandable.getExpansions()));
        return expansions;
    }

    /**
     * Brief Sets permissions on all Expandables.
     * If a parameter is null, then it is ignored
     * @param expandable expandable permission
     * @param applicable applicable permission
     */
    public void setPermissions(Boolean expandable, Boolean applicable) {
        if (expandable != null) getExpandableList().forEach(exp -> exp.setExpandable(expandable));
        if (applicable != null) getExpandableList().forEach(exp -> exp.setApplicable(applicable));
    }
}
