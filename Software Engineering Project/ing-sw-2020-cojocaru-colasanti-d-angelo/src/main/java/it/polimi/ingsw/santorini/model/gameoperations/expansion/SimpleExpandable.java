package it.polimi.ingsw.santorini.model.gameoperations.expansion;

import it.polimi.ingsw.santorini.model.gameoperations.exceptions.ExpansionAllowanceException;
import it.polimi.ingsw.santorini.model.gameoperations.exceptions.MarkerLessExpansionAdded;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Brief Represents a simple expandable implementation. Simple means that expansions are not treated different
 * based on their ExpansionType. They are all stored in the unique same list when added
 * @param <T> The type of the object that refines the expandable's behaviour
 * @param <S> The type of the supported expansions' marker
 * @param <U> The type of supported expansions
 */
public abstract class SimpleExpandable<T, S, U extends Expansion<T, S>>
        extends Expandable<T, S, U, SimpleExpandable<T, S, U>> {

    /**
     * Brief The unique list of expansions where added expansions are stored
     */
    protected List<U> expansions = new ArrayList<>();

    /**
     * Brief constructor that initializes the expandable and applicable state of the Expandable.
     * Resets also the filter's state of the expandable
     *
     * @param expandable Boolean true if expansions allowed
     * @param applicable Boolean true if applications on expansions allowed
     */
    public SimpleExpandable(Boolean expandable, Boolean applicable) {
        super(expandable, applicable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addExpansion(U expansion, Integer index) throws ExpansionAllowanceException, MarkerLessExpansionAdded {
        requiresExpandable();
        if (expansion.getMarkers() == null || expansion.getMarkers().isEmpty()) throw new MarkerLessExpansionAdded();
        expansions.add(index, expansion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeExpansion(Integer index) throws ExpansionAllowanceException {
        requiresExpandable();
        expansions.remove(index.intValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<U> getExpansions() {
        return expansions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer size() {
        return expansions.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(SimpleExpandable<T, S, U> expandable) throws ExpansionAllowanceException {
        requiresExpandable();
        expansions.addAll(expandable.expansions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void empty() throws ExpansionAllowanceException {
        requiresExpandable();
        expansions = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeExpansionsByFilter(Predicate<S> undesired) throws ExpansionAllowanceException {
        removeExpansionsByFilter(expansions, undesired);
    }
}
